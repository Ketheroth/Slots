package com.ketheroth.slots.platform.forge;

import com.ketheroth.slots.platform.WrappedEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ForgeWrappedEntry<T> implements WrappedEntry<T> {

	private final RegistryObject<T> object;

	public ForgeWrappedEntry(RegistryObject<T> object) {
		this.object = object;
	}

	@Override
	public ResourceLocation getId() {
		return object.getId();
	}

	@Override
	public T get() {
		return object.get();
	}

}
