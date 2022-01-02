package com.ketheroth.client.gui.screen.inventory;

import com.ketheroth.common.inventory.container.SlotsContainer;
import com.ketheroth.core.Slots;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SlotsInventoryScreen extends AbstractContainerScreen<SlotsContainer> {

	private final ResourceLocation GUI = new ResourceLocation(Slots.MODID, "textures/gui/slots_inventory.png");

	private final int slotAmount;

	public SlotsInventoryScreen(SlotsContainer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		slotAmount = container.getSlotAmount();
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int x, int y) {
		super.renderLabels(poseStack, x, y);
		this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
		this.font.draw(poseStack, playerInventoryTitle.getContents(), this.inventoryLabelX, this.inventoryLabelY, 4210752);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI);
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		this.blit(poseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
		for (int i = 0; i < slotAmount; i++) {
			int x = i % 9;
			int y = i / 9;
			this.blit(poseStack, relX + 7 + x * 18, relY + 16 + y * 18, 7, 83, 18, 18);
		}
	}

}
