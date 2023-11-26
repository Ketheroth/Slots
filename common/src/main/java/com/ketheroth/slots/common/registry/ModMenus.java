package com.ketheroth.slots.common.registry;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.inventory.container.SlotsMenu;
import com.ketheroth.slots.platform.WrappedEntry;
import com.ketheroth.slots.platform.WrappedRegistries;
import com.ketheroth.slots.platform.WrappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

	public static final WrappedRegistry<MenuType<?>> MENUS = WrappedRegistries.create(BuiltInRegistries.MENU, Slots.MOD_ID);

	public static final WrappedEntry<MenuType<SlotsMenu>> SLOTS_CONTAINER = MENUS.register("slots_container",() -> new MenuType<>((i, inventory) -> new SlotsMenu(i, inventory, inventory.player), FeatureFlags.VANILLA_SET));

}
