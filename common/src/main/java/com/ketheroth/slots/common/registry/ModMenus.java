package com.ketheroth.slots.common.registry;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.inventory.container.SlotsMenu;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

	public static final ResourcefulRegistry<MenuType<?>> MENUS = ResourcefulRegistries.create(BuiltInRegistries.MENU, Slots.MOD_ID);

	public static final RegistryEntry<MenuType<SlotsMenu>> SLOTS_CONTAINER = MENUS.register("slots_container",() -> new MenuType<>((i, inventory) -> new SlotsMenu(i, inventory, inventory.player), FeatureFlags.VANILLA_SET));

}
