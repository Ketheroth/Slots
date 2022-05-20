package com.ketheroth.common.network;

import com.ketheroth.common.network.message.MessageOpenSlots;
import com.ketheroth.common.network.message.MessageSyncToClient;
import com.ketheroth.core.Slots;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Slots.MODID, "network"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	public static void init() {
		INSTANCE.registerMessage(0, MessageOpenSlots.class, (msg, buf) -> {}, buf -> new MessageOpenSlots(), MessageOpenSlots::handle);
		INSTANCE.registerMessage(1, MessageSyncToClient.class, (msg, buf) -> buf.writeNbt(msg.getCompound()), buf -> new MessageSyncToClient(buf.readNbt()), MessageSyncToClient::handle);
	}

}
