package com.ketheroth.slots.platform.fabric;

import com.ketheroth.slots.platform.WrappedEntry;
import net.minecraft.resources.ResourceLocation;

public class FabricWrappedEntry<T> implements WrappedEntry<T> {

	private final ResourceLocation id;
	private final T object;

	public FabricWrappedEntry(ResourceLocation id, T object) {
		this.id = id;
		this.object = object;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public T get() {
		return object;
	}

}
