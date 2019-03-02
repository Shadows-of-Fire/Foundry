package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import net.minecraftforge.fluids.FluidStack;

public interface IAlloyingCrucibleRecipeManager {

	/**
	 * Register an Alloying Crucible recipe.
	 * @param out Output.
	 * @param fluidA Input A.
	 * @param fluidB Input B.
	 */
	public void addRecipe(FluidStack out, FluidStack fluidA, FluidStack fluidB);

	/**
	 * Find a valid recipe that contains the given inputs.
	 * A recipe is found if the recipe's inputs contains the fluid in the parameters.
	 * @param fluidA FluidStack for the first input.
	 * @param fluidB FluidStack for the second input.
	 * @return
	 */
	public IAlloyingCrucibleRecipe findRecipe(FluidStack fluidA, FluidStack fluidB);

	/**
	 * Get a list of all the recipes
	 * @return List of all the recipes
	 */
	public List<IAlloyingCrucibleRecipe> getRecipes();

	/**
	 * Removes a recipe.
	 * @param The recipe to remove.
	 */
	public void removeRecipe(IAlloyingCrucibleRecipe recipe);
}
