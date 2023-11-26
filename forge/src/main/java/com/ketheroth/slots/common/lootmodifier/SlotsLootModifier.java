package com.ketheroth.slots.common.lootmodifier;

import com.ketheroth.slots.common.config.SlotsConfig;
import com.ketheroth.slots.common.registry.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class SlotsLootModifier extends LootModifier {

	public static final Codec<SlotsLootModifier> CODEC = RecordCodecBuilder.create(inst ->
			codecStart(inst).and(
					Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)
			).apply(inst, SlotsLootModifier::new)
	);


	public final float chance;

	public SlotsLootModifier(LootItemCondition[] conditionsIn, float chance) {
		super(conditionsIn);
		this.chance = chance;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (SlotsConfig.generateItemReward && context.getRandom().nextFloat() <= chance) {
			generatedLoot.add(new ItemStack(ModItems.SLOT_REWARD.get()));
		}
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}

}
