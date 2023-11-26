package com.ketheroth.slots.forge;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.client.screen.SlotsInventoryScreen;
import com.ketheroth.slots.common.config.forge.ForgeMenuConfig;
import com.ketheroth.slots.common.registry.ModItems;
import com.ketheroth.slots.common.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Slots.MOD_ID, value = Dist.CLIENT)
public class SlotsForgeClient {

	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		MenuScreens.register(ModMenus.SLOTS_CONTAINER.get(), SlotsInventoryScreen::new);
		ForgeMenuConfig.register();
	}

}
