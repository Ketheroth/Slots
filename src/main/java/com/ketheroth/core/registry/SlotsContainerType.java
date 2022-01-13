package com.ketheroth.core.registry;

import com.ketheroth.common.inventory.container.SlotsContainer;
import com.ketheroth.core.Slots;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SlotsContainerType {

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Slots.MODID);

	public static final RegistryObject<ContainerType<SlotsContainer>> SLOTS_CONTAINER = CONTAINERS.register("slots_container",
			() -> IForgeContainerType.create((windowId, inv, data) -> new SlotsContainer(windowId, inv.player)));

}
