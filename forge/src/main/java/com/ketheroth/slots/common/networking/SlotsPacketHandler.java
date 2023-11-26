package com.ketheroth.slots.common.networking;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.network.OpenSlotPacket;
import com.ketheroth.slots.common.network.SyncPlayerDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class SlotsPacketHandler {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Slots.MOD_ID, "network"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	public static void init() {
		INSTANCE.registerMessage(0, OpenSlotPacket.class, OpenSlotPacket::write, buf -> new OpenSlotPacket(), (packet, contextSupplier) -> {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(()  -> packet.handle(context.getSender()));
			context.setPacketHandled(true);
		});
		INSTANCE.registerMessage(1, SyncPlayerDataPacket.class, SyncPlayerDataPacket::write, buf -> {
			SyncPlayerDataPacket packet = new SyncPlayerDataPacket();
			packet.read(buf);
			return packet;
		}, (packet, ctx) -> {
			ctx.get().enqueueWork(() ->
					DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeSyncPlayerDataPacket.handle(packet))
			);
			ctx.get().setPacketHandled(true);
		});
	}

}
