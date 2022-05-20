package com.ketheroth.common.capability;

public class SlotUnlocker {

	private final boolean removable;
	private final int slotAmount;

	public SlotUnlocker(boolean removable, int slotAmount) {
		this.removable = removable;
		this.slotAmount = slotAmount;
	}

	public boolean isRemovable() {
		return this.removable;
	}

	public int slotAmount() {
		return this.slotAmount;
	}

}
