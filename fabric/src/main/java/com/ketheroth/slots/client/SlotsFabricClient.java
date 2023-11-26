package com.ketheroth.slots.client;

import com.ketheroth.slots.client.keymapping.SlotKeyMapping;
import com.ketheroth.slots.client.screen.SlotsInventoryScreen;
import com.ketheroth.slots.common.networking.FabricOpenSlotPacket;
import com.ketheroth.slots.common.networking.FabricSyncPlayerDataPacket;
import com.ketheroth.slots.common.registry.ModMenus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;

public class SlotsFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenus.SLOTS_CONTAINER.get(), SlotsInventoryScreen::new);
		KeyBindingHelper.registerKeyBinding(SlotKeyMapping.KEY_OPEN);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (SlotKeyMapping.KEY_OPEN.isDown()) {
				ClientPlayNetworking.send(new FabricOpenSlotPacket());
			}
		});
		ClientPlayNetworking.registerGlobalReceiver(FabricSyncPlayerDataPacket.TYPE, (packet, player, responseSender) -> packet.handle(player));
	}

}
