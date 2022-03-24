package com.ketheroth.core.other;

import com.ketheroth.common.capability.SlotUnlocker;
import com.ketheroth.common.config.Configuration;
import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageSyncToClient;
import com.ketheroth.core.registry.SlotsCapabilities;
import com.ketheroth.common.capability.SlotsCapabilityProvider;
import com.ketheroth.core.Slots;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

import java.util.List;

@Mod.EventBusSubscriber(modid = Slots.MODID)
public class SlotsEvents {

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof Player)) {
			return;
		}
		if (event.getObject().getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).isPresent()) {
			return;
		}
		SlotsCapabilityProvider provider = new SlotsCapabilityProvider();
		event.addCapability(new ResourceLocation(Slots.MODID, "slots"), provider);
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			serverPlayer.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
				for (int i = 0; i < capability.getStacks().getSlots(); i++) {
					ItemStack stack = capability.getStacks().extractItem(i, ItemStack.EMPTY.getMaxStackSize(), false);
					if (!stack.isEmpty()) {
						ItemEntity itementity = new ItemEntity(serverPlayer.level, serverPlayer.getX(), serverPlayer.getEyeY(), serverPlayer.getZ(), stack);
						itementity.setPickUpDelay(40);
						itementity.setThrower(serverPlayer.getUUID());
						serverPlayer.level.addFreshEntity(itementity);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			Player oldPlayer = event.getOriginal();
			oldPlayer.revive();
			oldPlayer.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(oldCap -> {
				event.getPlayer().getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(newCap -> {
					newCap.deserializeNBT(oldCap.serializeNBT());
					int removedSlots = oldPlayer.experienceLevel / Configuration.LEVEL_PER_SLOT.get();
					for (int i = 0; i < removedSlots; i++) {
						//no need to drop items from removed slots as they should have already been dropped when the player died
						newCap.removeFirstUnlocker(unlocker -> unlocker.isRemovable() && unlocker.slotAmount() == 1);
					}
				});
			});
		} else {
			event.getOriginal().getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(oldCap -> {
				event.getPlayer().getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(newCap -> {
					newCap.deserializeNBT(oldCap.serializeNBT());
				});
			});
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
			serverPlayer.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
				NetworkHandler.INSTANCE.sendTo(new MessageSyncToClient(capability.serializeNBT()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			serverPlayer.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
				NetworkHandler.INSTANCE.sendTo(new MessageSyncToClient(capability.serializeNBT()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onXpLevelChange(PlayerXpEvent.LevelChange event) {
		Player player = event.getPlayer();
		if (player instanceof ServerPlayer serverPlayer) {
			player.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
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

	private static void addOrDropItems(ServerPlayer player, List<ItemStack> stacks) {
		stacks.forEach(stack -> {
			if (!player.getInventory().add(stack)) {
				// can't add to player inventory, drop the item
				ItemEntity itementity = new ItemEntity(player.level, player.getX(), player.getEyeY(), player.getZ(), stack);
				itementity.setPickUpDelay(40);
				itementity.setThrower(player.getUUID());
				player.level.addFreshEntity(itementity);
			}
		});
	}

}
