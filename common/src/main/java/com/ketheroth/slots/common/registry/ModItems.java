package com.ketheroth.slots.common.registry;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.item.SlotRewardItem;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ModItems {

	public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Slots.MOD_ID);

	public static final RegistryEntry<Item> SLOT_REWARD = ITEMS.register("slot_reward", SlotRewardItem::new);

}
