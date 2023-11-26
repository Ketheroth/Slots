package com.ketheroth.slots.platform.forge;

import com.ketheroth.slots.platform.WrappedEntry;
import com.ketheroth.slots.platform.WrappedRegistry;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ForgeWrappedRegistry<T> implements WrappedRegistry<T> {

	private final DeferredRegister<T> registry;
	private final List<WrappedEntry<T>> entries;

	public ForgeWrappedRegistry(Registry<T> registry, String modid) {
		this.registry = DeferredRegister.create(registry.key(), modid);
		entries = new ArrayList<>();
	}

	@Override
	public <I extends T> WrappedEntry<I> register(String id, Supplier<I> supplier) {
		ForgeWrappedEntry<T> entry = new ForgeWrappedEntry<>(registry.register(id, supplier));
		entries.add(entry);
		return (WrappedEntry<I>) entry;
	}

	@Override
	public Collection<WrappedEntry<T>> getEntries() {
		return entries;
	}

	@Override
	public void init() {
		registry.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
