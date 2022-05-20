package com.ketheroth.core.registry;

import com.ketheroth.common.item.SlotRewardItem;
import com.ketheroth.core.Slots;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SlotsItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Slots.MODID);

	public static final RegistryObject<Item> SLOT_REWARD = ITEMS.register("slot_reward", SlotRewardItem::new);

}
