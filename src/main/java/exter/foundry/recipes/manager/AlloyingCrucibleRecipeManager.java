package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.recipes.AlloyingCrucibleRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class AlloyingCrucibleRecipeManager {

	private static final NonNullList<IAlloyingCrucibleRecipe> RECIPES = NonNullList.create();

	public static void addRecipe(IAlloyingCrucibleRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static void addRecipe(FluidStack out, FluidStack in_a, FluidStack in_b) {
		RECIPES.add(new AlloyingCrucibleRecipe(out, in_a, in_b));
	}

	public static IAlloyingCrucibleRecipe findRecipe(FluidStack in_a, FluidStack in_b) {
		for (IAlloyingCrucibleRecipe r : RECIPES) {
			if (r.matchesRecipe(in_a, in_b)) { return r; }
		}
		return null;
	}

	public static List<IAlloyingCrucibleRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public static void removeRecipe(IAlloyingCrucibleRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
