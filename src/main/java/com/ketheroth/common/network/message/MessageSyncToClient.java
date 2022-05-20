package com.ketheroth.common.network.message;

import com.ketheroth.common.capability.SlotsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncToClient {

	private final CompoundNBT compound;

	public MessageSyncToClient(CompoundNBT compound) {
		this.compound = compound;
	}

	public CompoundNBT getCompound() {
		return compound;
	}

	public static void handle(MessageSyncToClient message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}
			player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(capability -> capability.deserializeNBT(message.compound));
		});
	}

}
