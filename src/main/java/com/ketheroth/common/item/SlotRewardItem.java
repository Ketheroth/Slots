package com.ketheroth.common.item;

import com.ketheroth.common.capability.SlotUnlocker;
import com.ketheroth.common.config.Configuration;
import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageSyncToClient;
import com.ketheroth.core.registry.SlotsCapabilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;

import javax.annotation.Nonnull;

public class SlotRewardItem extends Item {

	public SlotRewardItem() {
		super(new Item.Properties().tab(CreativeModeTab.TAB_MISC));
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand usedHand) {
		ItemStack itemStack = player.getItemInHand(usedHand);
		player.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
			if (!level.isClientSide) {
				capability.addUnlocker(new SlotUnlocker(!Configuration.PRESERVE_REWARD_ON_DEATH.get(), 1));
				NetworkHandler.INSTANCE.sendTo(new MessageSyncToClient(capability.serializeNBT()), ((ServerPlayer) player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			}
			if (!player.getAbilities().instabuild) {
				itemStack.shrink(1);
			}
		});
		if (player.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).isPresent()) {
			return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
		}
		return InteractionResultHolder.fail(itemStack);
	}

}
