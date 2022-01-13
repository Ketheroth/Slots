package com.ketheroth.core.keymapping;

import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.common.network.message.MessageOpenSlots;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.network.NetworkDirection;
import org.lwjgl.glfw.GLFW;

public class SlotsKeyMapping {

	public static final KeyBinding KEY_OPEN = new KeyBinding("key.slots.inventory", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_N), "slots.key.category");

	public static void handleKeyMapping(InputEvent.KeyInputEvent event) {
		if (KEY_OPEN.isDown()) {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}
			NetworkHandler.INSTANCE.sendTo(new MessageOpenSlots(), player.connection.getConnection(), NetworkDirection.PLAY_TO_SERVER);
		}
	}

}
