package com.ketheroth.core.registry;

import com.ketheroth.common.inventory.container.SlotsContainer;
import com.ketheroth.core.Slots;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SlotsContainerType {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Slots.MODID);

	public static final RegistryObject<MenuType<SlotsContainer>> SLOTS_CONTAINER = CONTAINERS.register("slots_container",
			() -> IForgeMenuType.create((windowId, inv, data) -> new SlotsContainer(windowId, inv.player)));

}
