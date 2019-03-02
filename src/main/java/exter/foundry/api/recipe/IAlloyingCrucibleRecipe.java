package exter.foundry.api.recipe;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.FluidStack;

public interface IAlloyingCrucibleRecipe {
	/**
	 * Get the recipe's input A.
	 */
	FluidStack getInputA();

	/**
	 * Get the recipe's input B.
	 */
	FluidStack getInputB();

	/**
	 * Get the recipe's output.
	 */
	FluidStack getOutput();

	/**
	 * Check if the fluids matches this recipe.
	 * @param fluidA fluid to compare.
	 * @param fluidB fluid to compare.
	 * @return true if the fluids matches, false otherwise.
	 */
	default boolean matchesRecipe(@Nonnull FluidStack fluidA, @Nonnull FluidStack fluidB) {
		return fluidA.containsFluid(getInputA()) && fluidB.containsFluid(getInputB());
	}
}
