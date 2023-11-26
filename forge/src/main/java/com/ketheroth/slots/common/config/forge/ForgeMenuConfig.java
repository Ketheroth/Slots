package com.ketheroth.slots.common.config.forge;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.config.SlotsConfig;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ForgeMenuConfig {

	/**
	 * Register our configuration menu to the modlist menu
	 */
	public static void register() {
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
					ResourcefulConfig config = Slots.CONFIGURATOR.getConfig(SlotsConfig.class);
					if (config == null) {
						return null;
					}
					return new ConfigScreen(null, config);
				})
		);
	}

}
