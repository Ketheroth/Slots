package com.ketheroth.slots.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface HandledPacket {

	void write(FriendlyByteBuf buf);

	void read(FriendlyByteBuf buf);

	void handle(Player player);

}
