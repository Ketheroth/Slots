package com.ketheroth.slots.common.utils.fabric;

import com.ketheroth.slots.common.networking.FabricSyncPlayerDataPacket;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class PlatformUtilsImpl {

	public static void syncPlayerData(ServerPlayer player) {
		SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(player);
		player.server.execute(() -> ServerPlayNetworking.send(player, new FabricSyncPlayerDataPacket(playerData)));
	}


//	public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(PlatformUtils.MenuFactory<T> factory) {
//		return new ExtendedScreenHandlerType<>(factory::create);
//	}
//
//	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
//		player.openMenu(new ExtendedScreenHandlerFactory() {
//			@Override
//			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
//				provider.writeExtraData(player, buf);
//			}
//
//			@Override
//			public Component getDisplayName() {
//				return provider.getDisplayName();
//			}
//
//			@Nullable
//			@Override
//			public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
//				return provider.createMenu(i, inventory, player);
//			}
//		});
//	}

}
