package com.ketheroth.slots.common.networking;

import com.ketheroth.slots.common.network.SyncPlayerDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSyncPlayerDataPacket {
	public static void handle(SyncPlayerDataPacket packet) {
		packet.handle(Minecraft.getInstance().player);
	}

}
