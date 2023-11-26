package com.ketheroth.slots.common.utils.forge;

import com.ketheroth.slots.common.network.SyncPlayerDataPacket;
import com.ketheroth.slots.common.networking.SlotsPacketHandler;
import com.ketheroth.slots.common.utils.ExtraDataMenuProvider;
import com.ketheroth.slots.common.utils.PlatformUtils;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class PlatformUtilsImpl {

	public static void syncPlayerData(ServerPlayer player) {
		SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(player);
		SlotsPacketHandler.INSTANCE.sendTo(new SyncPlayerDataPacket(playerData), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

//	public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(PlatformUtils.MenuFactory<T> factory) {
//		return IForgeMenuType.create(factory::create);
//	}
//
//	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
//		NetworkHooks.openScreen(player, provider, (data) -> provider.writeExtraData(player, data));
//	}

}
