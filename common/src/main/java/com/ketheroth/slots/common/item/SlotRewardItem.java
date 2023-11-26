package com.ketheroth.slots.common.item;

import com.ketheroth.slots.common.utils.PlatformUtils;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SlotRewardItem extends Item {

	public SlotRewardItem() {
		super(new Item.Properties());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(player);
		ItemStack itemStack = player.getItemInHand(usedHand);
		if (playerData.getTotalUnlockedSlots() >= 27) {
			return InteractionResultHolder.fail(itemStack);
		}
		if (level.isClientSide) {
			return InteractionResultHolder.success(itemStack);
		}
		player.sendSystemMessage(Component.literal("You gained 1 slot"));
		playerData.addUnremovableSlot();
		PlatformUtils.syncPlayerData(((ServerPlayer) player));
		return InteractionResultHolder.consume(itemStack);
	}

}
