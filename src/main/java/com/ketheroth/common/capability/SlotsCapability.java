package com.ketheroth.common.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.items.ItemStackHandler;

public class SlotsCapability {

	public static final Capability<ItemStackHandler> PLAYER_SLOT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	public static void register(RegisterCapabilitiesEvent event) {
		event.register(ItemStackHandler.class);
	}

}
