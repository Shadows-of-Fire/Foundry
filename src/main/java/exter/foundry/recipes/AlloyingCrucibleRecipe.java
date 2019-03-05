package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import net.minecraftforge.fluids.FluidStack;

public class AlloyingCrucibleRecipe implements IAlloyingCrucibleRecipe {

	protected final FluidStack inputA;
	protected final FluidStack inputB;
	protected final FluidStack output;

	public AlloyingCrucibleRecipe(FluidStack output, FluidStack inputA, FluidStack inputB) {
		this.inputA = Preconditions.checkNotNull(inputA, "Alloying Crucible Recipe input may not be null.");
		this.inputB = Preconditions.checkNotNull(inputB, "Alloying Crucible Recipe input may not be null.");
		this.output = Preconditions.checkNotNull(output, "Alloying Crucible Recipe output may not be null.");
	}

	@Override
	public FluidStack getInputA() {
		return inputA;
	}

	@Override
	public FluidStack getInputB() {
		return inputB;
	}

	@Override
	public FluidStack getOutput() {
		return output;
	}

}
