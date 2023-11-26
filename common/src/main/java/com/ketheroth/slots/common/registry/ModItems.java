package com.ketheroth.slots.common.registry;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.item.SlotRewardItem;
import com.ketheroth.slots.platform.WrappedEntry;
import com.ketheroth.slots.platform.WrappedRegistries;
import com.ketheroth.slots.platform.WrappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ModItems {

	public static final WrappedRegistry<Item> ITEMS = WrappedRegistries.create(BuiltInRegistries.ITEM, Slots.MOD_ID);

	public static final WrappedEntry<Item> SLOT_REWARD = ITEMS.register("slot_reward", SlotRewardItem::new);

}
