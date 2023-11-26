package com.ketheroth.slots.common.networking;

import com.ketheroth.slots.common.network.OpenSlotPacket;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.resources.ResourceLocation;

public class FabricOpenSlotPacket extends OpenSlotPacket implements FabricPacket {

	public static PacketType<FabricOpenSlotPacket> TYPE = PacketType.create(new ResourceLocation("slots:open_slot"), buf -> {
		FabricOpenSlotPacket packet = new FabricOpenSlotPacket();
		packet.read(buf);
		return packet;
	});

	@Override
	public PacketType<FabricOpenSlotPacket> getType() {
		return TYPE;
	}


}
