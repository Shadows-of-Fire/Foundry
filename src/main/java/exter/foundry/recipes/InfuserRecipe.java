package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.IInfuserRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class InfuserRecipe implements IInfuserRecipe {

	protected final Ingredient input;
	protected final FluidStack inFluid;
	protected final FluidStack output;
	protected final int energy;

	public InfuserRecipe(Ingredient input, FluidStack inFluid, FluidStack output, int energy) {
		this.input = Preconditions.checkNotNull(input, "Infuser Recipe input may not be null.");
		this.inFluid = Preconditions.checkNotNull(inFluid, "Infuser Recipe input fluid may not be null.");
		this.output = Preconditions.checkNotNull(output, "Infuser Recipe output may not be null.");
		Preconditions.checkArgument(energy > 0, "Infuse Recipe energy cost must be positive.");
		this.energy = energy;
	}

	@Override
	public Ingredient getInput() {
		return input;
	}

	@Override
	public FluidStack getInputFluid() {
		return inFluid;
	}

	@Override
	public FluidStack getOutput() {
		return output;
	}

	@Override
	public int getEnergyCost() {
		return energy;
	}

}
