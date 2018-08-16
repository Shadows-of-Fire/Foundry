package exter.foundry.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import shadows.placebo.Placebo;

public class FluidLiquidMetal extends Fluid {

	private int color;

	public final boolean special;

	public FluidLiquidMetal(String fluidName, ResourceLocation still, ResourceLocation flowing, int color, boolean special, int temperature, int luminosity) {
		super(fluidName, still, flowing);
		this.color = color | 0xFF000000;
		this.special = special;
		setTemperature(temperature);
		setLuminosity(luminosity);
		setDensity(2000);
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public String getLocalizedName(FluidStack stack) {
		return Placebo.PROXY.translate(this.getUnlocalizedName(stack) + ".name");
	}

	@Override
	public FluidLiquidMetal setColor(int fluid_color) {
		color = fluid_color | 0xFF000000;
		return this;
	}
}
