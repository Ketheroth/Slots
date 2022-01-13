package com.ketheroth.common.network.message;

import com.ketheroth.common.capability.SlotsCapability;
import com.ketheroth.common.capability.SlotsCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageLevelChanged {

	private final int level;

	public MessageLevelChanged(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public static void handle(MessageLevelChanged message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}
			player.getCapability(SlotsCapability.PLAYER_SLOT_CAPABILITY).ifPresent(itemStackHandler -> {
				SlotsCapabilityProvider.changeSize(itemStackHandler, message.level);
			});
		});
	}

}
