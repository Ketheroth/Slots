package com.ketheroth.slots.platform.fabric;

import com.ketheroth.slots.platform.WrappedEntry;
import com.ketheroth.slots.platform.WrappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class FabricWrappedRegistry<T> implements WrappedRegistry<T> {

	private final Registry<T> registry;
	private final String modid;
	private final List<WrappedEntry<T>> entries;

	public FabricWrappedRegistry(Registry<T> registry, String modid) {
		this.registry = registry;
		this.modid = modid;
		entries = new ArrayList<>();
	}

	@Override
	public <I extends T> WrappedEntry<I> register(String id, Supplier<I> supplier) {
		ResourceLocation rl = new ResourceLocation(this.modid, id);
		I object = Registry.register(this.registry, rl, supplier.get());
		FabricWrappedEntry<I> entry = new FabricWrappedEntry<>(rl, object);
		entries.add((WrappedEntry<T>) entry);
		return entry;
	}

	@Override
	public Collection<WrappedEntry<T>> getEntries() {
		return entries;
	}

	@Override
	public void init() {
	}

}
