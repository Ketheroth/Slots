package com.ketheroth.slots.common.events;

import com.ketheroth.slots.common.utils.PlatformUtils;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class ServerEvents {

	public static void onPlayerDeath(LivingEntity entity, DamageSource damageSource) {
		if (entity instanceof ServerPlayer player) {
			SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(player);
			for (ItemStack stack : playerData.inventory.removeAllItems()) {
				ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getEyeY(), player.getZ(), stack);
				itementity.setPickUpDelay(40);
				itementity.setThrower(player.getUUID());
				player.level().addFreshEntity(itementity);
			}
			playerData.resetAfterDeath();
			PlatformUtils.syncPlayerData(player);
		}
	}

}
