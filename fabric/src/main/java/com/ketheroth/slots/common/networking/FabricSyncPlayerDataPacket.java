package com.ketheroth.slots.common.networking;

import com.ketheroth.slots.common.network.SyncPlayerDataPacket;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.resources.ResourceLocation;

public class FabricSyncPlayerDataPacket extends SyncPlayerDataPacket implements FabricPacket {

	public static PacketType<FabricSyncPlayerDataPacket> TYPE = PacketType.create(new ResourceLocation("slots:sync_slots"), buf -> {
		FabricSyncPlayerDataPacket packet = new FabricSyncPlayerDataPacket();
		packet.read(buf);
		return packet;
	});

	public FabricSyncPlayerDataPacket() {

	}

	public FabricSyncPlayerDataPacket(SlotsSavedData.PlayerData playerData) {
		super(playerData);
	}

	@Override
	public PacketType<FabricSyncPlayerDataPacket> getType() {
		return TYPE;
	}

}
