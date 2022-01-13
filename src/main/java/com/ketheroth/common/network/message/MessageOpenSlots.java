package com.ketheroth.common.network.message;

import com.ketheroth.common.inventory.container.SlotsContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MessageOpenSlots {


	public MessageOpenSlots() {
	}

	public static void handle(MessageOpenSlots message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> {
			ServerPlayerEntity player = context.getSender();
			if (player != null) {
				INamedContainerProvider provider = new INamedContainerProvider() {

					@Override
					public ITextComponent getDisplayName() {
						return new TranslationTextComponent("screen.slots.slots_inventory");
					}

					@Nullable
					@Override
					public Container createMenu(int containerId, PlayerInventory inventory, PlayerEntity player) {
						return new SlotsContainer(containerId, player);
					}
				};
				NetworkHooks.openGui(player, provider);
			}
		});
	}

}
