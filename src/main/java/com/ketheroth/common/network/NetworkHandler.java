package com.ketheroth.common.network;

import com.ketheroth.common.network.message.MessageLevelChanged;
import com.ketheroth.common.network.message.MessageOpenSlots;
import com.ketheroth.core.Slots;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
		INSTANCE.registerMessage(1, MessageLevelChanged.class, (msg, buf) -> buf.writeInt(msg.getLevel()), buf -> new MessageLevelChanged(buf.readInt()), MessageLevelChanged::handle);
	}

}