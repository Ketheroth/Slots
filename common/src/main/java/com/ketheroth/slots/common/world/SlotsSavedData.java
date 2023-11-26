package com.ketheroth.slots.common.world;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.config.SlotsConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.UUID;

public class SlotsSavedData extends SavedData {

	public static PlayerData clientData = new PlayerData();


	public HashMap<UUID, PlayerData> players = new HashMap<>();

	public static PlayerData getPlayerUnlockedSlots(LivingEntity player) {
		if (player.level().isClientSide) {
			return clientData;
		}
		return getServerData(player.getServer()).players.computeIfAbsent(player.getUUID(), uuid -> new PlayerData());
	}


	public static SlotsSavedData getServerData(MinecraftServer server) {
		DimensionDataStorage dataStorage = server.getLevel(Level.OVERWORLD).getDataStorage();
		SlotsSavedData state = dataStorage.computeIfAbsent(SlotsSavedData::createFromTag, SlotsSavedData::new, Slots.MOD_ID);
		state.setDirty();
		return state;
	}

	public static SlotsSavedData createFromTag(CompoundTag tag) {
		SlotsSavedData state = new SlotsSavedData();
		CompoundTag playersTag = tag.getCompound("Players");
		playersTag.getAllKeys().forEach(key -> {
			CompoundTag compound = playersTag.getCompound(key);
			PlayerData playerData = new PlayerData();
			playerData.load(compound);
			state.players.put(UUID.fromString(key), playerData);
		});
		return state;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		CompoundTag playersTag = new CompoundTag();
		players.forEach((uuid, playerData) -> {
			CompoundTag data = new CompoundTag();
			playerData.save(data);
			playersTag.put(uuid.toString(), data);
		});
		tag.put("Players", playersTag);
		return tag;
	}

	public static class PlayerData {

		public SimpleContainer inventory;
		private int unlockedSlots;
		private int unremovableSlots;

		public PlayerData() {
			this(0, 0);
		}

		public PlayerData(int unlockedSlots, int unremovableSlots) {
			this.unlockedSlots = unlockedSlots;
			this.unremovableSlots = unremovableSlots;
			this.inventory = new SimpleContainer(unlockedSlots);
		}

		public void addSlot() {
			this.unlockedSlots++;
			SimpleContainer inv = new SimpleContainer(unlockedSlots);
			for (int i = 0; i < this.inventory.getContainerSize(); i++) {
				inv.setItem(i, this.inventory.getItem(i));
			}
			this.inventory = inv;
		}

		public void addUnremovableSlot() {
			this.addSlot();
			this.unremovableSlots++;
		}

		public void load(CompoundTag tag) {
			this.unlockedSlots = tag.getInt("UnlockedSlots");
			this.unremovableSlots = tag.getInt("UnremovableSlots");
			this.inventory = new SimpleContainer(this.unlockedSlots);
			this.inventory.fromTag(tag.getList("Inventory", Tag.TAG_COMPOUND));
		}

		public void save(CompoundTag tag) {
			tag.putInt("UnlockedSlots", this.unlockedSlots);
			tag.putInt("UnremovableSlots", this.unremovableSlots);
			ListTag inv = this.inventory.createTag();
			tag.put("Inventory", inv);
		}

		public int getTotalUnlockedSlots() {
			return this.unlockedSlots;
		}

		public int getXpUnlockedSlots() {
			return this.unlockedSlots - this.unremovableSlots;
		}

		public ItemStack removeSlot() {
			if (this.unlockedSlots > this.unremovableSlots) {
				this.unlockedSlots--;
				SimpleContainer inv = new SimpleContainer(unlockedSlots);
				for (int i = 0; i < this.inventory.getContainerSize() - 1; i++) {
					inv.setItem(i, this.inventory.getItem(i));
				}
				ItemStack stack = this.inventory.removeItem(this.unlockedSlots, this.inventory.getMaxStackSize());
				this.inventory = inv;
				return stack;
			}
			return ItemStack.EMPTY;
		}

		public void resetAfterDeath() {
			if (SlotsConfig.preserveRewardsOnDeath) {
				this.unlockedSlots = this.unremovableSlots;
			} else {
				this.unlockedSlots = 0;
				this.unremovableSlots = 0;
			}
			this.inventory = new SimpleContainer(this.unlockedSlots);
		}

	}

}
