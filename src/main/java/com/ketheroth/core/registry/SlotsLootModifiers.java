package com.ketheroth.core.registry;

import com.ketheroth.common.lootmodifier.SlotLootModifier;
import com.ketheroth.core.Slots;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SlotsLootModifiers {

	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLOBAL_LOOT_MODIFIER_SERIALIZER = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Slots.MODID);

	public static final RegistryObject<SlotLootModifier.Serializer> CHEST_LOOT = GLOBAL_LOOT_MODIFIER_SERIALIZER.register("chest_loot_modifier", SlotLootModifier.Serializer::new);

}
