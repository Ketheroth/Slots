package com.ketheroth.common.inventory.container;

import com.ketheroth.common.capability.SlotsCapability;
import com.ketheroth.core.registry.SlotsContainerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class SlotsContainer extends Container {

	private final IItemHandler playerInventory;
	private IItemHandler slotsInventory = null;

	public SlotsContainer(int containerId, PlayerEntity player) {
		super(SlotsContainerType.SLOTS_CONTAINER.get(), containerId);
		this.playerInventory = new InvWrapper(player.inventory);
		player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> {
			slotsInventory = capability.getStacks();
			// layout slots inventory
			int slot = 0;
			int size = slotsInventory.getSlots();
			while (slot < size) {
				int x = slot % 9;
				int y = slot / 9;
				this.addSlot(new SlotItemHandler(this.slotsInventory, slot, 8 + 18 * x, 17 + 18 * y));
				slot++;
			}
		});
		// layout player inventory
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new SlotItemHandler(this.playerInventory, 9 + 9 * y + x, 8 + 18 * x, 84 + 18 * y));
			}
		}
		for (int i = 0; i < 9; i++) {
			this.addSlot(new SlotItemHandler(this.playerInventory, i, 8 + 18 * i, 142));
		}
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return slotsInventory != null;
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		int size = this.slotsInventory.getSlots();
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
		return slotsInventory.getSlots();
	}

}
