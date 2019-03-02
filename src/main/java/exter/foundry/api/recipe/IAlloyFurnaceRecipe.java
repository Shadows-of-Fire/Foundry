package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public interface IAlloyFurnaceRecipe {

	/**
	 * Get the recipe's input A by index.
	 * @return Recipe's input A.
	 */
	Ingredient getInputA();

	/**
	 * Get the recipe's input B by index.
	 * @return Recipe's input B.
	 */
	Ingredient getInputB();

	/**
	 * Get the recipe's output.
	 * @return ItemStack containing recipe's output.
	 */
	ItemStack getOutput();

	/**
	 * Check if the items matches this recipe.
	 * @param input_a item to compare.
	 * @param input_b item to compare.
	 * @return true if the items matches, false otherwise.
	 */
	default boolean matchesRecipe(ItemStack inputA, ItemStack inputB) {
		return getInputA().apply(inputA) && getInputB().apply(inputB);
	}
}
