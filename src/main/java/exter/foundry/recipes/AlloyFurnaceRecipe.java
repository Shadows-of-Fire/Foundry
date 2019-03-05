package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class AlloyFurnaceRecipe implements IAlloyFurnaceRecipe {

	protected final Ingredient inA;
	protected final Ingredient inB;
	protected final ItemStack output;

	public AlloyFurnaceRecipe(ItemStack output, Ingredient inA, Ingredient inB) {
		this.inA = Preconditions.checkNotNull(inA, "Alloy Furnace Recipe input may not be null.");
		this.inB = Preconditions.checkNotNull(inB, "Alloy Furnace Recipe input may not be null.");
		this.output = Preconditions.checkNotNull(output, "Alloy Furnace Recipe output may not be null.");
	}

	@Override
	public Ingredient getInputA() {
		return inA;
	}

	@Override
	public Ingredient getInputB() {
		return inB;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

}
