package com.ketheroth.slots.client;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.client.keymapping.SlotKeyMapping;
import com.ketheroth.slots.common.network.OpenSlotPacket;
import com.ketheroth.slots.common.networking.SlotsPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = Slots.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public static void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {
		event.register(SlotKeyMapping.KEY_OPEN);
	}

	@SubscribeEvent
	public static void onKeyPress(InputEvent.Key event) {
		if (SlotKeyMapping.KEY_OPEN.isDown()) {
			LocalPlayer player = Minecraft.getInstance().player;
			SlotsPacketHandler.INSTANCE.sendTo(new OpenSlotPacket(), player.connection.getConnection(), NetworkDirection.PLAY_TO_SERVER);
		}
	}

}
