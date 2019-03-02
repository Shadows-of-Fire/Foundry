package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public interface IAlloyFurnaceRecipeManager {

	/**
	 * Register an Alloy Mixer recipe.
	 * @param out Output.
	 * @param inA Input A.
	 * @param inB Input B.
	 */
	public void addRecipe(ItemStack out, Ingredient inA, Ingredient inB);

	/**
	 * Finds a recipe.
	 * @param inA ItemStack for the first input.
	 * @param inB ItemStack for the second input.
	 * @return A recipe that matches these inputs.
	 */
	public IAlloyFurnaceRecipe findRecipe(ItemStack inA, ItemStack inB);

	/**
	 * Get a list of all the recipes
	 * @return List of all the recipes
	 */
	public List<IAlloyFurnaceRecipe> getRecipes();

	/**
	 * Removes a recipe.
	 * @param The recipe to remove.
	 */
	public void removeRecipe(IAlloyFurnaceRecipe recipe);
}
