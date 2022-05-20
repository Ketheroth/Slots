package com.ketheroth.core;

import com.ketheroth.client.gui.screen.inventory.SlotsInventoryScreen;
import com.ketheroth.common.capability.PlayerSlots;
import com.ketheroth.common.capability.SlotsCapability;
import com.ketheroth.common.config.Configuration;
import com.ketheroth.common.network.NetworkHandler;
import com.ketheroth.core.keymapping.SlotsKeyMapping;
import com.ketheroth.core.registry.SlotsContainerType;
import com.ketheroth.core.registry.SlotsItems;
import com.ketheroth.core.registry.SlotsLootModifiers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Slots.MODID)
public class Slots {

	public static final String MODID = "slots";
	public static final Logger LOGGER = LogManager.getLogger();


	public Slots() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);

		SlotsContainerType.CONTAINERS.register(modEventBus);
		SlotsItems.ITEMS.register(modEventBus);
		SlotsLootModifiers.GLOBAL_LOOT_MODIFIER_SERIALIZER.register(modEventBus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.CONFIG);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.addListener(SlotsKeyMapping::handleKeyMapping);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(NetworkHandler::init);
		CapabilityManager.INSTANCE.register(PlayerSlots.class, new SlotsCapability.Storage(), PlayerSlots::new);
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		ScreenManager.register(SlotsContainerType.SLOTS_CONTAINER.get(), SlotsInventoryScreen::new);
		ClientRegistry.registerKeyBinding(SlotsKeyMapping.KEY_OPEN);
	}

}
