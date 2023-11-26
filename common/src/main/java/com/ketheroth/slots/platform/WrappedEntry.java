package com.ketheroth.slots.platform;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface WrappedEntry<T> extends Supplier<T> {

//	@Override
//	T get();

	ResourceLocation getId();

}