package com.ketheroth.core.keymapping;

import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageOpenSlots;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.network.NetworkDirection;
import org.lwjgl.glfw.GLFW;

public class SlotsKeyMapping {

	public static final KeyMapping KEY_OPEN = new KeyMapping("key.slots.inventory", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_N), "slots.key.category");

	public static void handleKeyMapping(InputEvent.KeyInputEvent event) {
		if (KEY_OPEN.isDown()) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}
			NetworkHandler.INSTANCE.sendTo(new MessageOpenSlots(), player.connection.getConnection(), NetworkDirection.PLAY_TO_SERVER);
		}
	}

}
