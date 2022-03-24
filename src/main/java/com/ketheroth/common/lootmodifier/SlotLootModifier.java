package com.ketheroth.common.lootmodifier;

import com.google.gson.JsonObject;
import com.ketheroth.common.config.Configuration;
import com.ketheroth.core.registry.SlotsItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlotLootModifier extends LootModifier {
	private final List<ResourceLocation> validLootTables = List.of(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"),
			new ResourceLocation("minecraft", "chests/buried_treasure"),
			new ResourceLocation("minecraft", "chests/end_city_treasure"),
			new ResourceLocation("minecraft", "chests/shipwreck_treasure"),
			new ResourceLocation("minecraft", "chests/simple_dungeon"),
			new ResourceLocation("minecraft", "chests/stronghold_corridor"),
			new ResourceLocation("minecraft", "chests/stronghold_crossing"));

	protected SlotLootModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@NotNull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		System.out.println("applying");
		if (!Configuration.XP_ONLY.get() && validLootTables.contains(context.getQueriedLootTableId())) {
			if (context.getRandom().nextInt(3) * (1+context.getLuck()) > 1) {
				generatedLoot.add(new ItemStack(SlotsItems.SLOT_REWARD.get(), 1));
			}
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<SlotLootModifier> {

		@Override
		public SlotLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
			return new SlotLootModifier(ailootcondition);
		}

		@Override
		public JsonObject write(SlotLootModifier instance) {
			return makeConditions(instance.conditions);
		}

	}
}
