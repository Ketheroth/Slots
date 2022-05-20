package com.ketheroth.common.item;

import com.ketheroth.common.capability.SlotUnlocker;
import com.ketheroth.common.capability.SlotsCapability;
import com.ketheroth.common.config.Configuration;
import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageSyncToClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;

public class SlotRewardItem extends Item {

	public SlotRewardItem() {
		super(new Item.Properties().tab(ItemGroup.TAB_MISC));
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> use(@Nonnull World level, @Nonnull PlayerEntity player, @Nonnull Hand usedHand) {
		ItemStack itemStack = player.getItemInHand(usedHand);
		player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
			if (!level.isClientSide) {
				capability.addUnlocker(new SlotUnlocker(!Configuration.PRESERVE_REWARD_ON_DEATH.get(), 1));
				NetworkHandler.INSTANCE.sendTo(new MessageSyncToClient(capability.serializeNBT()), ((ServerPlayerEntity) player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			}
			if (!player.abilities.instabuild) {
				itemStack.shrink(1);
			}
		});
		if (player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).isPresent()) {
			return ActionResult.sidedSuccess(itemStack, level.isClientSide);
		}
		return ActionResult.fail(itemStack);
	}

}
