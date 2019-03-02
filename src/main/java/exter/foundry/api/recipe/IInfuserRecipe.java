package exter.foundry.api.recipe;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public interface IInfuserRecipe {

	/**
	 * Get the substance required.
	 * @return The substance required.
	 */
	Ingredient getInput();

	/**
	 * Get the fluid required.
	 * @return FluidStack containing the required fluid.
	 */
	FluidStack getInputFluid();

	/**
	 * Get the produced fluid.
	 * @return The fluid that the recipe produces.
	 */
	FluidStack getOutput();

	/**
	 * @return How much FE this recipe takes.
	 */
	int getEnergyCost();

	/**
	 * Check if a fluid stack and substance stack matches this recipe
	 * @param fluid Fluid to check.
	 * @param item Itemstack type to check.
	 * @return true if the fluid and substance matches, false otherwise.
	 */
	default boolean matchesRecipe(@Nonnull FluidStack fluid, ItemStack item) {
		return fluid.containsFluid(getInputFluid()) && getInput().apply(item);
	}
}
