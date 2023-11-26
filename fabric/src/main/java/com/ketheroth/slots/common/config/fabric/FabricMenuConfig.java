package com.ketheroth.slots.common.config.fabric;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.config.SlotsConfig;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class FabricMenuConfig implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ResourcefulConfig config = Slots.CONFIGURATOR.getConfig(SlotsConfig.class);
			if (config == null) {
				return null;
			}
			return new ConfigScreen(null, config);
		};
	}

}
