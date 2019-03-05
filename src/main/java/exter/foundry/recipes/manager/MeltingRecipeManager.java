package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.recipes.MeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class MeltingRecipeManager {

	private static final NonNullList<IMeltingRecipe> RECIPES = NonNullList.create();

	public static void addRecipe(IMeltingRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static void addRecipe(FluidStack output, Ingredient input, int temp, int speed) {
		addRecipe(new MeltingRecipe(output, input, temp, speed));
	}

	public static void addRecipe(FluidStack output, Ingredient input, int temp) {
		addRecipe(output, input, temp, 100);
	}

	public static void addRecipe(FluidStack output, Ingredient input) {
		addRecipe(output, input, output.getFluid().getTemperature());
	}

	public static IMeltingRecipe findRecipe(ItemStack item) {
		if (item.isEmpty()) return null;
		for (IMeltingRecipe r : RECIPES) {
			if (r.matchesRecipe(item)) return r;
		}
		return null;
	}

	public static List<IMeltingRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public static void removeRecipe(IMeltingRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
