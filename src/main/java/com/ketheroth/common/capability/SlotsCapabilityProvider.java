package com.ketheroth.common.capability;

import com.ketheroth.common.config.Configuration;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SlotsCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

	private final ItemStackHandler items = new ItemStackHandler(0);
	private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> items);

	public void invalidate() {
		optional.invalidate();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == SlotsCapability.PLAYER_SLOT_CAPABILITY) {
			return optional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		if (SlotsCapability.PLAYER_SLOT_CAPABILITY != null) {
			tag.put("SlotsContent", items.serializeNBT());
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT tag) {
		if (SlotsCapability.PLAYER_SLOT_CAPABILITY != null) {
			items.deserializeNBT(tag.getCompound("SlotsContent"));
		}
	}

	public static List<ItemStack> changeSize(ItemStackHandler items, int level) {
		int size = level == 0 ? 0 : level / Configuration.LEVEL_PER_SLOT.get();
		if (size > 27) {
			size = 27;
		}
		int oldSize = items.getSlots();
		Queue<ItemStack> copy = new ArrayDeque<>(oldSize);
		for (int i = 0; i < items.getSlots(); i++) {
			copy.offer(items.getStackInSlot(i).copy());
		}
		items.setSize(size);
		for (int i = 0; i < oldSize && i < items.getSlots(); i++) {
			ItemStack stack = copy.poll();
			items.setStackInSlot(i, stack == null ? ItemStack.EMPTY : stack);
		}
		return new ArrayList<>(copy);
	}

}
