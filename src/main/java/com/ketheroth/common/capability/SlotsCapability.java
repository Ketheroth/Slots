package com.ketheroth.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SlotsCapability {

	@CapabilityInject(ItemStackHandler.class)
	public static Capability<ItemStackHandler> PLAYER_SLOT_CAPABILITY = null;

	public static class Storage implements Capability.IStorage<ItemStackHandler> {

		@Nullable
		@Override
		public INBT writeNBT(Capability<ItemStackHandler> capability, ItemStackHandler instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.put("SlotsContent", instance.serializeNBT());
			return nbt;
		}

		@Override
		public void readNBT(Capability<ItemStackHandler> capability, ItemStackHandler instance, Direction side, INBT nbt) {
			instance.deserializeNBT(((CompoundNBT) nbt).getCompound("SlotsContent"));
		}

	}
}
