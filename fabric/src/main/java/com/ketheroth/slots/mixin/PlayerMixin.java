package com.ketheroth.slots.mixin;

import com.ketheroth.slots.common.config.SlotsConfig;
import com.ketheroth.slots.common.networking.FabricSyncPlayerDataPacket;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

	private static void addOrDropItems(Player player, ItemStack stack) {
		if (!player.getInventory().add(stack)) {
			// can't add to player inventory, drop the item
			ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getEyeY(), player.getZ(), stack);
			itementity.setPickUpDelay(40);
			itementity.setThrower(player.getUUID());
			player.level().addFreshEntity(itementity);
		}
	}

	@Inject(method = "giveExperienceLevels", at = @At("TAIL"))
	private void onLevelAdded(int levels, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if (player.level().isClientSide) {
			return;
		}
		SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(player);
		int newSlotAmount = player.experienceLevel / SlotsConfig.levelPerSlot;
		int delta = newSlotAmount - playerData.getXpUnlockedSlots();
		if (delta == 0) {
			return;
		}
		if (delta > 0) {
			for (int i = 0; i < delta; i++) {
				playerData.addSlot();
			}
		} else {
			for (int i = delta; i < 0; i++) {
				ItemStack stack = playerData.removeSlot();
				if (!stack.isEmpty()) {
					addOrDropItems(player, stack);
				}
			}
		}
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.server.execute(() -> ServerPlayNetworking.send(serverPlayer, new FabricSyncPlayerDataPacket(playerData)));
		}
	}

}
