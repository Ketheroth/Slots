package com.ketheroth.slots.platform;


import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface WrappedRegistry<T> {

	<I extends T> WrappedEntry<I> register(String id, Supplier<I> supplier);

	Collection<WrappedEntry<T>> getEntries();

	default Stream<WrappedEntry<T>> stream() {
		return getEntries().stream();
	}

	default Stream<T> boundStream() {
		return stream().map(WrappedEntry::get);
	}

	void init();

}
