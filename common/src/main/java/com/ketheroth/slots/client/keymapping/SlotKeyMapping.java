package com.ketheroth.slots.client.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;

public class SlotKeyMapping {

	public static final KeyMapping KEY_OPEN = new KeyMapping("key.slots.inventory", InputConstants.KEY_N, "slots.key.category");

	public static void handleKeyPress(LocalPlayer player) {
		if (KEY_OPEN.isDown()) {
		}
	}
}
