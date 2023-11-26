package com.ketheroth.slots.forge;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.config.SlotsConfig;
import com.ketheroth.slots.common.events.ServerEvents;
import com.ketheroth.slots.common.lootmodifier.SlotsLootModifier;
import com.ketheroth.slots.common.network.SyncPlayerDataPacket;
import com.ketheroth.slots.common.networking.SlotsPacketHandler;
import com.ketheroth.slots.common.registry.ModItems;
import com.ketheroth.slots.common.world.SlotsSavedData;
import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Slots.MOD_ID)
@Mod.EventBusSubscriber
public class SlotsForge {

	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Slots.MOD_ID);


	public SlotsForge() {
		Slots.init();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(SlotsForge::onCommonSetup);
//		bus.addListener(SlotsForge::onClientSetup);
		bus.addListener(SlotsForge::onCreativeTabBuild);
		GLOBAL_LOOT_MODIFIER_SERIALIZER.register(bus);
		GLOBAL_LOOT_MODIFIER_SERIALIZER.register("chest_loot_modifier", () -> SlotsLootModifier.CODEC);
	}

	public static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(SlotsPacketHandler::init);
	}



	public static void onCreativeTabBuild(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			event.accept(ModItems.SLOT_REWARD.get());
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(serverPlayer);
			SlotsPacketHandler.INSTANCE.sendTo(new SyncPlayerDataPacket(playerData), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
		ServerEvents.onPlayerDeath(event.getEntity(), event.getSource());
	}

	@SubscribeEvent
	public static void onXpLevelChange(PlayerXpEvent.LevelChange event) {
		Player player = event.getEntity();
		if (player.level().isClientSide) {
			return;
		}
		SlotsSavedData.PlayerData playerData = SlotsSavedData.getPlayerUnlockedSlots(player);
		int experienceLevel = player.experienceLevel + event.getLevels();
		if (experienceLevel < 0) {
			experienceLevel = 0;
		}
		int newSlotAmount = experienceLevel / SlotsConfig.levelPerSlot;
		int delta = newSlotAmount - playerData.getXpUnlockedSlots();
		if (delta == 0) {
			return;
		}
		if (delta > 0) {
			for (int i = 0; i < delta; i++) {
				playerData.addSlot();
			}
		} else {
			for (int i = delta; i < 0; i++) {
				ItemStack stack = playerData.removeSlot();
				if (!stack.isEmpty()) {
					addOrDropItems(player, stack);
				}
			}
		}
		if (player instanceof ServerPlayer serverPlayer) {
			SlotsPacketHandler.INSTANCE.sendTo(new SyncPlayerDataPacket(playerData), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	private static void addOrDropItems(Player player, ItemStack stack) {
		if (!player.getInventory().add(stack)) {
			// can't add to player inventory, drop the item
			ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getEyeY(), player.getZ(), stack);
			itementity.setPickUpDelay(40);
			itementity.setThrower(player.getUUID());
			player.level().addFreshEntity(itementity);
		}
	}

}
