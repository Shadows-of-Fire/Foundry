package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.recipes.MoldRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class MoldRecipeManager {

	private static final NonNullList<IMoldRecipe> RECIPES = NonNullList.create();

	public static void addRecipe(IMoldRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static void addRecipe(ItemStack result, int width, int height, int[] recipe) {
		RECIPES.add(new MoldRecipe(result, width, height, recipe));
	}

	public static IMoldRecipe findRecipe(int[] grid) {
		for (IMoldRecipe r : RECIPES) {
			if (r.matchesRecipe(grid)) { return r; }
		}
		return null;
	}

	public static List<IMoldRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public static void removeRecipe(IMoldRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
