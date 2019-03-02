package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IMeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public interface IMeltingRecipeManager {

	/**
	 * Register a Melting Crucible recipe.
	 * Uses the fluid's temperature as it's melting point.
	 * @param solid The item to be melted
	 * @param fluid_stack Resulting fluid
	 */
	public void addRecipe(Ingredient input, FluidStack output);

	/**
	 * Register a Melting Crucible recipe.
	 * @param solid The item to be melted
	 * @param fluid_stack Resulting fluid
	 * @param melting_point Temperature required for the item to melt. Must be >295 and <5000
	 */
	public void addRecipe(Ingredient input, FluidStack output, int temp);

	/**
	 * Register a Melting Crucible recipe.
	 * @param solid The item to be melted
	 * @param fluid_stack Resulting fluid
	 * @param melting_point Temperature required for the item to melt. Must be >295 and <5000
	 * @param melting_speed. Speed in which the item melts. Default is 100.
	 */
	public void addRecipe(Ingredient input, FluidStack output, int temp, int speed);

	/**
	 * Find a valid recipe that contains the given item
	 * @param item The item required in the recipe
	 * @return
	 */
	public IMeltingRecipe findRecipe(ItemStack item);

	/**
	 * Get a list of all the recipes
	 * @return List of all the recipes
	 */
	public List<IMeltingRecipe> getRecipes();

	/**
	 * Removes a recipe.
	 * @param The recipe to remove.
	 */
	public void removeRecipe(IMeltingRecipe recipe);
}
