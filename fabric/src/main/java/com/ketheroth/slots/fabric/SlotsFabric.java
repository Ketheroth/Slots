package com.ketheroth.slots.fabric;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.config.SlotsConfig;
import com.ketheroth.slots.common.events.ServerEvents;
import com.ketheroth.slots.common.networking.FabricOpenSlotPacket;
import com.ketheroth.slots.common.networking.FabricSyncPlayerDataPacket;
import com.ketheroth.slots.common.registry.ModItems;
import com.ketheroth.slots.common.world.SlotsSavedData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.List;

public class SlotsFabric implements ModInitializer {

	public static final List<ResourceLocation> VALID_LOOT_TABLES = List.of(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"),
			new ResourceLocation("minecraft", "chests/buried_treasure"),
			new ResourceLocation("minecraft", "chests/end_city_treasure"),
			new ResourceLocation("minecraft", "chests/shipwreck_treasure"),
			new ResourceLocation("minecraft", "chests/simple_dungeon"),
			new ResourceLocation("minecraft", "chests/stronghold_corridor"),
			new ResourceLocation("minecraft", "chests/stronghold_crossing"));


	@Override
	public void onInitialize() {
		Slots.init();
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(content -> content.accept(ModItems.SLOT_REWARD.get()));
		ServerPlayNetworking.registerGlobalReceiver(FabricOpenSlotPacket.TYPE, (openSlotPacket, player, responseSender) -> openSlotPacket.handle(player));
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(handler.player);
			server.execute(() -> ServerPlayNetworking.send(handler.player, new FabricSyncPlayerDataPacket(playerData)));
		});
		ServerLivingEntityEvents.AFTER_DEATH.register(ServerEvents::onPlayerDeath);
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (SlotsConfig.generateItemReward && VALID_LOOT_TABLES.contains(id) && source.isBuiltin()) {
				LootPool pool = LootPool.lootPool()
						.with(LootItem.lootTableItem(ModItems.SLOT_REWARD.get()).build())
						.conditionally(LootItemRandomChanceCondition.randomChance(0.66F).build())
						.build();
				tableBuilder.pool(pool);
			}
		});
	}

}
