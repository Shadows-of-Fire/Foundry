package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.recipes.AtomizerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class AtomizerRecipeManager {

	private static final NonNullList<IAtomizerRecipe> RECIPES = NonNullList.create();

	public static void addRecipe(IAtomizerRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static void addRecipe(ItemStack result, FluidStack input) {
		RECIPES.add(new AtomizerRecipe(result, input));
	}

	public static IAtomizerRecipe findRecipe(FluidStack fluid) {
		for (IAtomizerRecipe ar : RECIPES) {
			if (ar.matchesRecipe(fluid)) return ar;
		}
		return null;
	}

	public static List<IAtomizerRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public static void removeRecipe(IAtomizerRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
