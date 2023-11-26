package com.ketheroth.slots.common.utils;

import com.ketheroth.slots.common.network.HandledPacket;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.NotImplementedException;

public class PlatformUtils {

	@ExpectPlatform
	public static void syncPlayerData(ServerPlayer player) {
		throw new NotImplementedException();
	}


	@ExpectPlatform
	public static <T> void createRegistry(Registry<T> registry, String id) {
		throw new NotImplementedException();
	}


}
