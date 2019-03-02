package exter.foundry.api.recipe;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IAtomizerRecipe {

	/**
	 * Get the fluid required for atomizing.
	 * @return FluidStack containing the required fluid.
	 */
	FluidStack getInput();

	/**
	 * Get the actual item produced by atomizing.
	 * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
	 */
	ItemStack getOutput();

	/**
	 * Check if a fluid stack and mold matches this recipe.
	 * @param fluid_stack fluid to check (must contain the fluid in the recipe).
	 * @return true if the fluid matches, false otherwise.
	 */
	default boolean matchesRecipe(@Nonnull FluidStack fluid) {
		return fluid.containsFluid(getInput());
	}
}
