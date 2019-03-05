package exter.foundry.gui;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAlloyFurnace extends GuiFoundry {

	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/alloyfurnace.png");

	public static final int BURN_X = 49;
	public static final int BURN_Y = 37;
	public static final int BURN_WIDTH = 14;
	public static final int BURN_HEIGHT = 14;

	public static final int PROGRESS_X = 79;
	public static final int PROGRESS_Y = 35;
	public static final int PROGRESS_WIDTH = 22;
	public static final int PROGRESS_HEIGHT = 15;

	public static final int PROGRESS_OVERLAY_X = 176;
	public static final int PROGRESS_OVERLAY_Y = 14;

	public static final int BURN_OVERLAY_X = 176;
	public static final int BURN_OVERLAY_Y = 0;

	private final TileEntityAlloyFurnace furnace;

	public GuiAlloyFurnace(TileEntityAlloyFurnace furnace, EntityPlayer player) {
		super(new ContainerAlloyFurnace(furnace, player));
		allowUserInput = false;
		ySize = 166;
		this.furnace = furnace;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(GUI_TEXTURE);
		int window_x = (width - xSize) / 2;
		int window_y = (height - ySize) / 2;
		drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

		if (furnace.getFuelBurn() > 0) {
			int burn = furnace.getBurn() * PROGRESS_HEIGHT / furnace.getFuelBurn();

			if (burn > 0) {
				drawTexturedModalRect(window_x + BURN_X, window_y + BURN_Y + BURN_HEIGHT - burn, BURN_OVERLAY_X, BURN_OVERLAY_Y + BURN_HEIGHT - burn, BURN_WIDTH, burn);
			}
		}
		if (furnace.getProgress() > 0) {
			int progress = furnace.getProgress() * PROGRESS_WIDTH / 400;
			drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, progress, PROGRESS_HEIGHT);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y) {
		super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

		fontRenderer.drawString("Alloy Furnace", 5, 6, 0x404040);
		fontRenderer.drawString("Inventory", 8, ySize - 96 + 2, 0x404040);

	}

	@Override
	protected ResourceLocation getGUITexture() {
		return GUI_TEXTURE;
	}
}
