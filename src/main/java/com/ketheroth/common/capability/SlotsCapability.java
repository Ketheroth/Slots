package com.ketheroth.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SlotsCapability {

	@CapabilityInject(PlayerSlots.class)
	public static Capability<PlayerSlots> PLAYER_SLOT_CAPABILITY = null;

	public static class Storage implements Capability.IStorage<PlayerSlots> {

		@Nullable
		@Override
		public INBT writeNBT(Capability<PlayerSlots> capability, PlayerSlots instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.put("SlotsContent", instance.serializeNBT());
			return nbt;
		}

		@Override
		public void readNBT(Capability<PlayerSlots> capability, PlayerSlots instance, Direction side, INBT nbt) {
			instance.deserializeNBT(((CompoundNBT) nbt).getCompound("SlotsContent"));
		}

	}
}
