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

	public void addRecipe(IMeltingRecipe recipe) {
		RECIPES.add(recipe);
	}

	public void addRecipe(FluidStack output, Ingredient input, int temp, int speed) {
		addRecipe(new MeltingRecipe(output, input, temp, speed));
	}

	public void addRecipe(FluidStack output, Ingredient input, int temp) {
		addRecipe(output, input, temp, 100);
	}

	public void addRecipe(FluidStack output, Ingredient input) {
		addRecipe(output, input, output.getFluid().getTemperature());
	}

	public IMeltingRecipe findRecipe(ItemStack item) {
		if (item.isEmpty()) return null;
		for (IMeltingRecipe r : RECIPES) {
			if (r.matchesRecipe(item)) return r;
		}
		return null;
	}

	public List<IMeltingRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public void removeRecipe(IMeltingRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
