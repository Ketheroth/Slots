package com.ketheroth.core.other;

import com.ketheroth.common.capability.SlotsCapability;
import com.ketheroth.common.capability.SlotsCapabilityProvider;
import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageLevelChanged;
import com.ketheroth.core.Slots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
		SlotsCapabilityProvider provider = new SlotsCapabilityProvider();
		event.addCapability(new ResourceLocation(Slots.MODID, "slots"), provider);
		event.addListener(provider::invalidate);
	}


	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) event.getPlayer());
			event.getPlayer().getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(stackHandler -> {
				List<ItemStack> remaining = SlotsCapabilityProvider.changeSize(stackHandler, serverPlayer.experienceLevel);
				addOrDropItems(serverPlayer, remaining);
				NetworkHandler.INSTANCE.sendTo(new MessageLevelChanged(serverPlayer.experienceLevel), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) event.getEntity());
			serverPlayer.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(stackHandler -> {
				List<ItemStack> remaining = SlotsCapabilityProvider.changeSize(stackHandler, serverPlayer.experienceLevel);
				addOrDropItems(serverPlayer, remaining);
				NetworkHandler.INSTANCE.sendTo(new MessageLevelChanged(serverPlayer.experienceLevel), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onXpLevelChange(PlayerXpEvent.LevelChange event) {
		PlayerEntity player = event.getPlayer();
		if (player instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) player);
			player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(itemStackHandler -> {
				int newLevel = event.getPlayer().experienceLevel + event.getLevels();
				List<ItemStack> remaining = SlotsCapabilityProvider.changeSize(itemStackHandler, newLevel);
				addOrDropItems(serverPlayer, remaining);
				NetworkHandler.INSTANCE.sendTo(new MessageLevelChanged(newLevel), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
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
