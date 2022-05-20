package com.ketheroth.common.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

public class PlayerSlots implements INBTSerializable<CompoundNBT> {

	private List<SlotUnlocker> unlockers = new ArrayList<>();
	private ItemStackHandler stacks = new ItemStackHandler(0);

	public ItemStackHandler getStacks() {
		return this.stacks;
	}

	public void addUnlocker(SlotUnlocker unlocker) {
		unlockers.add(unlocker);
		updateSize();
	}

	public List<ItemStack> removeFirstUnlocker(Predicate<SlotUnlocker> predicate) {
		for (SlotUnlocker unlocker : unlockers) {
			if (predicate.test(unlocker)) {
				unlockers.remove(unlocker);
				return updateSize();
			}
		}
		return new ArrayList<>();
	}

	private List<ItemStack> updateSize() {
		int size = unlockers.stream().reduce(0, (acc, unlckr) -> acc + unlckr.slotAmount(), Integer::sum);
		if (size > 27) {
			size = 27;
		}
		int oldSize = stacks.getSlots();
		Queue<ItemStack> copy = new ArrayDeque<>(oldSize);
		for (int i = 0; i < stacks.getSlots(); i++) {
			copy.offer(stacks.getStackInSlot(i).copy());
		}
		stacks.setSize(size);
		for (int i = 0; i < oldSize && i < stacks.getSlots(); i++) {
			ItemStack stack = copy.poll();
			stacks.setStackInSlot(i, stack == null ? ItemStack.EMPTY : stack);
		}
		return new ArrayList<>(copy);
	}

	@Override
	public CompoundNBT serializeNBT() {
		ListNBT unlockersTag = new ListNBT();
		for (SlotUnlocker unlocker : unlockers) {
			CompoundNBT tag = new CompoundNBT();
			tag.putBoolean("removable", unlocker.isRemovable());
			tag.putInt("amount", unlocker.slotAmount());
			unlockersTag.add(tag);
		}

		CompoundNBT compound = new CompoundNBT();
		compound.put("Stacks", stacks.serializeNBT());
		compound.put("Unlockers", unlockersTag);
		return compound;
	}

	@Override
	public void deserializeNBT(CompoundNBT compound) {
		if (compound.contains("Unlockers")) {
			// new version
			ListNBT listTag = compound.getList("Unlockers", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < listTag.size(); i++) {
				CompoundNBT unlckrTag = listTag.getCompound(i);
				unlockers.add(new SlotUnlocker(unlckrTag.getBoolean("removable"), unlckrTag.getInt("amount")));
			}
			stacks.deserializeNBT(compound.getCompound("Stacks"));
		} else {
			// Old version. There was only the ItemStackHandler, and slots could be unlocked only via xp.
			stacks.deserializeNBT(compound);
			for (int i = 0; i < stacks.getSlots(); i++) {
				unlockers.add(new SlotUnlocker(true, 1));
			}
		}
	}

}
