package com.ketheroth.slots.common.inventory.container;

import com.ketheroth.slots.common.registry.ModMenus;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotsMenu extends AbstractContainerMenu {

	private SlotsSavedData.PlayerData playerData;


	public SlotsMenu(int windowId, Inventory playerInventory, Player player) {
		super(ModMenus.SLOTS_CONTAINER.get(), windowId);
		this.playerData = SlotsSavedData.getPlayerUnlockedSlots(player);
		// layout slots inventory
		int slot = 0;
		int size = this.playerData.inventory.getContainerSize();
		while (slot < size) {
			int x = slot % 9;
			int y = slot / 9;
			this.addSlot(new Slot(this.playerData.inventory, slot, 8 + 18 * x, 17 + 18 * y) {
				@Override
				public void set(ItemStack stack) {
					super.set(stack);
				}

				@Override
				public void onTake(Player player, ItemStack stack) {
					super.onTake(player, stack);
				}
			});
			slot++;
		}
//		});
		// layout player inventory
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInventory, 9 + 9 * y + x, 8 + 18 * x, 84 + 18 * y));
			}
		}
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + 18 * i, 142));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.playerData.inventory != null;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		int size = this.playerData.inventory.getContainerSize();
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			itemstack = slotStack.copy();
			if (0 <= index && index < size) {// shift-click from slots inventory
				if (!this.moveItemStackTo(slotStack, size, 36 + size, true)) {
					return ItemStack.EMPTY;
				}
			} else if (size <= index && index < 36 + size) {// shift-click from player inventory
				if (!this.moveItemStackTo(slotStack, 0, size, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (slotStack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotStack);
		}
		return itemstack;
	}

	public int getSlotAmount() {
		return this.playerData.inventory.getContainerSize();
	}

}
