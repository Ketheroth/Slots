package com.ketheroth.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotsCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

	private final PlayerSlots playerSlots = new PlayerSlots();
	private final LazyOptional<PlayerSlots> optional = LazyOptional.of(() -> playerSlots);

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
			tag.put("SlotsContent", playerSlots.serializeNBT());
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT tag) {
		if (SlotsCapability.PLAYER_SLOT_CAPABILITY != null) {
			playerSlots.deserializeNBT(tag.getCompound("SlotsContent"));
		}
	}

}
