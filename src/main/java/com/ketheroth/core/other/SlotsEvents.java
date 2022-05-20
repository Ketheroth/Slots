package com.ketheroth.core.other;

import com.ketheroth.common.capability.SlotUnlocker;
import com.ketheroth.common.capability.SlotsCapability;
import com.ketheroth.common.capability.SlotsCapabilityProvider;
import com.ketheroth.common.config.Configuration;
import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageSyncToClient;
import com.ketheroth.core.Slots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.List;

@Mod.EventBusSubscriber(modid = Slots.MODID)
public class SlotsEvents {

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof PlayerEntity)) {
			return;
		}
		if (event.getObject().getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).isPresent()) {
			return;
		}
		SlotsCapabilityProvider provider = new SlotsCapabilityProvider();
		event.addCapability(new ResourceLocation(Slots.MODID, "slots"), provider);
		event.addListener(provider::invalidate);
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
				for (int i = 0; i < capability.getStacks().getSlots(); i++) {
					ItemStack stack = capability.getStacks().extractItem(i, ItemStack.EMPTY.getMaxStackSize(), false);
					if (!stack.isEmpty()) {
						ItemEntity itementity = new ItemEntity(player.level, player.getX(), player.getEyeY(), player.getZ(), stack);
						itementity.setPickUpDelay(40);
						itementity.setThrower(player.getUUID());
						player.level.addFreshEntity(itementity);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			PlayerEntity oldPlayer = event.getOriginal();
			oldPlayer.revive();
			oldPlayer.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(oldCap -> {
				event.getPlayer().getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(newCap -> {
					newCap.deserializeNBT(oldCap.serializeNBT());
					int removedSlots = oldPlayer.experienceLevel / Configuration.LEVEL_PER_SLOT.get();
					for (int i = 0; i < removedSlots; i++) {
						//no need to drop items from removed slots as they should have already been dropped when the player died
						newCap.removeFirstUnlocker(unlocker -> unlocker.isRemovable() && unlocker.slotAmount() == 1);
					}
				});
			});
		} else {
			event.getOriginal().getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(oldCap -> {
				event.getPlayer().getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(newCap -> {
					newCap.deserializeNBT(oldCap.serializeNBT());
				});
			});
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) event.getPlayer());
			event.getPlayer().getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
				NetworkHandler.INSTANCE.sendTo(new MessageSyncToClient(capability.serializeNBT()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) event.getEntity());
			serverPlayer.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
				NetworkHandler.INSTANCE.sendTo(new MessageSyncToClient(capability.serializeNBT()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onXpLevelChange(PlayerXpEvent.LevelChange event) {
		PlayerEntity player = event.getPlayer();
		if (player instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
				int remainLevel = event.getPlayer().experienceLevel % Configuration.LEVEL_PER_SLOT.get();
				if (event.getLevels() > 0) {
					remainLevel+=event.getLevels();
					int newSlots = remainLevel / Configuration.LEVEL_PER_SLOT.get();
					for (int i = 0; i < newSlots; i++) {
						capability.addUnlocker(new SlotUnlocker(true, 1));
					}
				} else {
					int removedLevel = -event.getLevels() - remainLevel;
					int removedSlots = removedLevel / Configuration.LEVEL_PER_SLOT.get();
					for (int i = 0; i < removedSlots; i++) {
						List<ItemStack> removed = capability.removeFirstUnlocker(unlocker -> unlocker.isRemovable() && unlocker.slotAmount() == 1);
						addOrDropItems(serverPlayer, removed);
					}
				}
				NetworkHandler.INSTANCE.sendTo(new MessageSyncToClient(capability.serializeNBT()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	private static void addOrDropItems(ServerPlayerEntity player, List<ItemStack> stacks) {
		stacks.forEach(stack -> {
			if (!player.inventory.add(stack)) {
				// can't add to player inventory, drop the item
				ItemEntity itementity = new ItemEntity(player.level, player.getX(), player.getEyeY(), player.getZ(), stack);
				itementity.setPickUpDelay(40);
				itementity.setThrower(player.getUUID());
				player.level.addFreshEntity(itementity);
			}
		});
	}

}
