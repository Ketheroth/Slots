package com.ketheroth.common.capability;

import com.ketheroth.core.registry.SlotsCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotsCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	private final PlayerSlots playerSlots = new PlayerSlots();
	private final LazyOptional<PlayerSlots> optional = LazyOptional.of(() -> playerSlots);

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == SlotsCapabilities.PLAYER_SLOT_CAPABILITY) {
			return optional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		if (SlotsCapabilities.PLAYER_SLOT_CAPABILITY.isRegistered()) {
			tag.put("SlotsContent", playerSlots.serializeNBT());
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		if (SlotsCapabilities.PLAYER_SLOT_CAPABILITY.isRegistered()) {
			playerSlots.deserializeNBT(tag.getCompound("SlotsContent"));
		}
	}

}
