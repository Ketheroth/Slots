package com.ketheroth.slots.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import org.apache.commons.lang3.NotImplementedException;

public class WrappedRegistries {

	@ExpectPlatform
	public static <T> WrappedRegistry<T> create(Registry<T> registry, String modid) {
		throw new NotImplementedException();
	}

}
