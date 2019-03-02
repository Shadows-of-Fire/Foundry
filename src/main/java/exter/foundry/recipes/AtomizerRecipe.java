package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.IAtomizerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AtomizerRecipe implements IAtomizerRecipe {

	protected final FluidStack input;
	protected final ItemStack output;

	public AtomizerRecipe(FluidStack input, ItemStack output) {
		this.input = Preconditions.checkNotNull(input, "Atomizer Recipe input may not be null.");
		this.output = Preconditions.checkNotNull(output, "Atomizer Recipe output may not be null.");
	}

	@Override
	public FluidStack getInput() {
		return input;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

}
