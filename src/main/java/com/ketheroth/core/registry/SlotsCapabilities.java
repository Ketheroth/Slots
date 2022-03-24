package com.ketheroth.core.registry;

import com.ketheroth.common.capability.PlayerSlots;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SlotsCapabilities {

	public static final Capability<PlayerSlots> PLAYER_SLOT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	public static void register(RegisterCapabilitiesEvent event) {
		event.register(PlayerSlots.class);
	}

}
