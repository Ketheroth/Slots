package com.ketheroth.slots.common.network;

import com.ketheroth.slots.common.world.SlotsSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

// from server to client
public class SyncPlayerDataPacket implements HandledPacket {

	public SlotsSavedData.PlayerData playerData;

	public SyncPlayerDataPacket() {
	}

	public SyncPlayerDataPacket(SlotsSavedData.PlayerData playerData) {
		this.playerData = playerData;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		CompoundTag tag = new CompoundTag();
		this.playerData.save(tag);
		buf.writeNbt(tag);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		CompoundTag tag = buf.readNbt();
		this.playerData = new SlotsSavedData.PlayerData();
		if (tag != null) {
			this.playerData.load(tag);
		}
	}


	@Override
	public void handle(Player player) {
		SlotsSavedData.clientData = this.playerData;
	}

}
