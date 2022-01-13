package com.ketheroth.client.gui.screen.inventory;

import com.ketheroth.common.inventory.container.SlotsContainer;
import com.ketheroth.core.Slots;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SlotsInventoryScreen extends ContainerScreen<SlotsContainer> {

	private final ResourceLocation GUI = new ResourceLocation(Slots.MODID, "textures/gui/slots_inventory.png");

	private final int slotAmount;

	public SlotsInventoryScreen(SlotsContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		slotAmount = container.getSlotAmount();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int x, int y) {
		super.renderLabels(matrixStack, x, y);
		this.font.draw(matrixStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
		this.font.draw(matrixStack, this.title.getContents(), this.inventoryLabelX, this.inventoryLabelY, 4210752);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTick, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(GUI);
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
		for (int i = 0; i < slotAmount; i++) {
			int x = i % 9;
			int y = i / 9;
			this.blit(matrixStack, relX + 7 + x * 18, relY + 16 + y * 18, 7, 83, 18, 18);
		}
	}

}
