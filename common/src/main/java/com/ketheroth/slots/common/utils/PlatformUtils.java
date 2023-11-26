package com.ketheroth.slots.common.utils;

import com.ketheroth.slots.common.network.HandledPacket;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.NotImplementedException;

public class PlatformUtils {

	@ExpectPlatform
	public static void syncPlayerData(ServerPlayer player) {
		throw new NotImplementedException();
	}

//	@ExpectPlatform
//	public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> factory) {
//		throw new NotImplementedException();
//	}
//
//	@ExpectPlatform
//	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
//		throw new NotImplementedException();
//	}
//
//
//	@FunctionalInterface
//	public interface MenuFactory<T extends AbstractContainerMenu> {
//
//		/**
//		 * @param syncId    The internal id for the menu.
//		 * @param inventory The inventory of the player.
//		 * @param byteBuf   The extra packet data for the menu.
//		 * @return The created menu instance.
//		 */
//		T create(int syncId, Inventory inventory, FriendlyByteBuf byteBuf);
//
//	}

}
