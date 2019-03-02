package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IMoldRecipe {

	int getHeight();

	ItemStack getOutput();

	int[] getRecipeGrid();

	int getWidth();

	boolean matchesRecipe(int[] grid);
}
