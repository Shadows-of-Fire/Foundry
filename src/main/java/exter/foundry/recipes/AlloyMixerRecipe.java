package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import net.minecraftforge.fluids.FluidStack;

public class AlloyMixerRecipe implements IAlloyMixerRecipe {

	protected final FluidStack[] input;
	protected final FluidStack output;

	public AlloyMixerRecipe(FluidStack output, FluidStack... input) {
		this.input = Preconditions.checkNotNull(input, "Alloy Mixer Recipe input may not be null.");
		this.output = Preconditions.checkNotNull(output, "Alloy Mixer Recipe output may not be null.");
		Preconditions.checkArgument(input.length <= 4, "Alloy Mixer recipes do not support more than 4 inputs!");
		for (FluidStack f : input)
			Preconditions.checkNotNull(f, "Alloy Mixer Recipe input may not be null.");
	}

	@Override
	public FluidStack[] getInputs() {
		return input;
	}

	@Override
	public FluidStack getOutput() {
		return output;
	}

}
