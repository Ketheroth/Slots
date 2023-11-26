package com.ketheroth.slots.client.screen;

import com.ketheroth.slots.Slots;
import com.ketheroth.slots.common.inventory.container.SlotsMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SlotsInventoryScreen extends AbstractContainerScreen<SlotsMenu> {

	private final ResourceLocation GUI = new ResourceLocation(Slots.MOD_ID, "textures/gui/slots_inventory.png");

	private final int slotAmount;

	public SlotsInventoryScreen(SlotsMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		slotAmount = menu.getSlotAmount();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		// TODO: @ketheroth render next slot to unlock "unlock this slot by having x xp levels"
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
		for (int i = 0; i < slotAmount; i++) {
			int x = i % 9;
			int y = i / 9;
			guiGraphics.blit(GUI, relX + 7 + x * 18, relY + 16 + y * 18, 7, 83, 18, 18);
		}
	}

}
