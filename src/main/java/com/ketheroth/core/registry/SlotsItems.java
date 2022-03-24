package com.ketheroth.core.registry;

import com.ketheroth.common.item.SlotRewardItem;
import com.ketheroth.core.Slots;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SlotsItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Slots.MODID);

	public static final RegistryObject<Item> SLOT_REWARD = ITEMS.register("slot_reward", SlotRewardItem::new);

}
