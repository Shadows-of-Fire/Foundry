package exter.foundry.gui.button;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonFoundry extends GuiButton {
	private final ResourceLocation gui_texture;
	private final int texture_x;
	private final int texture_y;

	private final int texture_mo_x;
	private final int texture_mo_y;

	private int icon_x;
	private int icon_y;

	private int icon_width;
	private int icon_height;

	public GuiButtonFoundry(int id, int x, int y, int w, int h, ResourceLocation texture, int tex_x, int tex_y, int tex_mo_x, int tex_mo_y) {
		super(id, x, y, w, h, "");
		texture_x = tex_x;
		texture_y = tex_y;
		texture_mo_x = tex_mo_x;
		texture_mo_y = tex_mo_y;
		gui_texture = texture;
		icon_width = 0;
		icon_height = 0;
	}

	@Override
	public void drawButton(Minecraft mc, int x, int y, float partialTicks) {
		if (visible) {
			mc.getTextureManager().bindTexture(gui_texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int tex_x = texture_x;
			int tex_y = texture_y;

			if (x >= this.x && y >= this.y && x < this.x + width && y < this.y + height) {
				tex_x = texture_mo_x;
				tex_y = texture_mo_y;
			}

			drawTexturedModalRect(this.x, this.y, tex_x, tex_y, width, height);
			if (icon_width > 0 && icon_height > 0) {
				drawTexturedModalRect(this.x + (width - icon_width) / 2, this.y + (height - icon_height) / 2, icon_x, icon_y, icon_width, icon_height);
			}
		}
	}

	public GuiButtonFoundry setIconTexture(int x, int y, int w, int h) {
		icon_x = x;
		icon_y = y;
		icon_width = w;
		icon_height = h;
		return this;
	}
}
