package exter.foundry.api.recipe;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.fluids.FluidStack;

public interface IAlloyMixerRecipe {

	/**
	 * Get the recipe's inputs.
	 * @return Recipe's inputs.
	 */
	FluidStack[] getInputs();

	/**
	 * Get the recipe's output.
	 * @return FluidStack containing Recipe's produced fluid and amount.
	 */
	FluidStack getOutput();

	/**
	 * Check if a list of fluid stacks matches this recipe.
	 * @param in list of fluid stack to compare.
	 * @param order [Output] Order in which the input fluids are matched.
	 * @return true if the fluids matches, false otherwise.
	 */
	default boolean matchesRecipe(FluidStack[] inputs) {
		if (inputs.length != getInputs().length) return false;

		IntList matched = new IntArrayList();
		boolean[] used = new boolean[inputs.length];
		for (int i = 0; i < getInputs().length; i++) {
			for (int ix = 0; ix < inputs.length; i++) {
				if (!used[ix] && inputs[ix].containsFluid(getInputs()[i])) {
					matched.add(i);
					used[ix] = true;
				}
			}
		}
		return matched.size() == getInputs().length;
	}
}
