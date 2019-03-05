package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingRecipe {

	/**
	 * Get the casting speed.
	 * @return The casting speed.
	 */
	int getCastingSpeed();

	/**
	 * Get the fluid required for casting.
	 * @return FluidStack containing the required fluid.
	 */
	FluidStack getInput();

	/**
	 * Get the extra item required for casting.
	 * @return The ingredient to match for extra items.  May be {@link Ingredient#EMPTY}
	 */
	Ingredient getItemInput();

	/**
	 * Get the mold required for casting.
	 * @return Ingredient matcher for the required mold.
	 */
	Ingredient getMold();

	/**
	 * Get the actual item produced by casting.
	 * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
	 */
	ItemStack getOutput();

	/**
	 * Check if a fluid stack and mold matches this recipe.
	 * @param mold_stack mold to check.
	 * @param fluid_stack fluid to check (must contain the fluid in the recipe).
	 * @return true if the stack and mold matches, false otherwise.
	 */
	default boolean matchesRecipe(FluidStack input, ItemStack item, ItemStack mold) {
		return getMold().apply(mold) && input.containsFluid(getInput()) && getItemInput().apply(item);
	}
}
