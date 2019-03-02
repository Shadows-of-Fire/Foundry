package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public interface IMeltingRecipe {

	/**
	 * Get the required item.
	 */
	Ingredient getInput();

	/**
	 * Get the melting temperature of the item.
	 * @return Melting temperature in K.
	 */
	int getMeltingPoint();

	/**
	 * Get the melting speed.
	 * @return The melting speed.
	 */
	int getMeltingSpeed();

	/**
	 * Get the produced fluid.
	 * @return The fluid that the recipe produces.
	 */
	FluidStack getOutput();

	/**
	 * Check if an item matches this recipe.
	 * @param item The item to check.
	 * @return true, if the item matches, false otherwise.
	 */
	default boolean matchesRecipe(ItemStack item) {
		return getInput().apply(item);
	}
}
