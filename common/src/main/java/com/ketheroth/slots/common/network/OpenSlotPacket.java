package com.ketheroth.slots.common.network;

import com.ketheroth.slots.common.inventory.container.SlotsMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

// from client to server
public class OpenSlotPacket implements HandledPacket {

	@Override
	public void write(FriendlyByteBuf buf) {

	}

	@Override
	public void read(FriendlyByteBuf buf) {

	}

	@Override
	public void handle(Player player) {
		player.openMenu(new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return Component.translatable("screen.slots.slots_inventory");
			}

			@Override
			public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
				return new SlotsMenu(i, inventory, player);
			}
		});
	}

}
