package com.ketheroth.common.network.message;

import com.ketheroth.core.registry.SlotsCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncToClient {

	private final CompoundTag compound;

	public MessageSyncToClient(CompoundTag compound) {
		this.compound = compound;
	}

	public CompoundTag getCompound() {
		return compound;
	}

	public static void handle(MessageSyncToClient message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}
			player.getCapability(SlotsCapabilities.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> capability.deserializeNBT(message.compound));
		});
	}

}
