package com.ketheroth.core.other;

import com.ketheroth.common.capability.SlotsCapability;
import com.ketheroth.common.capability.SlotsCapabilityProvider;
import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageLevelChanged;
import com.ketheroth.core.Slots;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
		SlotsCapabilityProvider provider = new SlotsCapabilityProvider();
		event.addCapability(new ResourceLocation(Slots.MODID, "slots"), provider);
		event.addListener(provider::invalidate);
	}


	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
			event.getPlayer().getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(stackHandler -> {
				List<ItemStack> remaining = SlotsCapabilityProvider.changeSize(stackHandler, serverPlayer.experienceLevel);
				addOrDropItems(serverPlayer, remaining);
				NetworkHandler.INSTANCE.sendTo(new MessageLevelChanged(serverPlayer.experienceLevel), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			serverPlayer.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(stackHandler -> {
				List<ItemStack> remaining = SlotsCapabilityProvider.changeSize(stackHandler, serverPlayer.experienceLevel);
				addOrDropItems(serverPlayer, remaining);
				NetworkHandler.INSTANCE.sendTo(new MessageLevelChanged(serverPlayer.experienceLevel), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}

	@SubscribeEvent
	public static void onXpLevelChange(PlayerXpEvent.LevelChange event) {
		Player player = event.getPlayer();
		if (player instanceof ServerPlayer serverPlayer) {
			player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(itemStackHandler -> {
				int newLevel = event.getPlayer().experienceLevel + event.getLevels();
				List<ItemStack> remaining = SlotsCapabilityProvider.changeSize(itemStackHandler, newLevel);
				addOrDropItems(serverPlayer, remaining);
				NetworkHandler.INSTANCE.sendTo(new MessageLevelChanged(newLevel), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
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
