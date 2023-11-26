package com.ketheroth.slots.platform.forge;

import com.ketheroth.slots.platform.WrappedRegistry;
import net.minecraft.core.Registry;

public class WrappedRegistriesImpl {

	public static <T> WrappedRegistry<T> create(Registry<T> registry, String modid) {
		return new ForgeWrappedRegistry<>(registry, modid);
	}
}
