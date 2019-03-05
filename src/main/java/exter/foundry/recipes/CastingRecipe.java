package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.ICastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class CastingRecipe implements ICastingRecipe {

	protected final FluidStack input;
	protected final Ingredient item;
	protected final Ingredient mold;
	protected final ItemStack output;
	protected final int speed;

	public CastingRecipe(ItemStack output, FluidStack input, Ingredient item, Ingredient mold, int speed) {
		this.input = Preconditions.checkNotNull(input, "Casting Recipe input may not be null.");
		this.item = Preconditions.checkNotNull(item, "Casting Recipe item input may not be null.");
		this.mold = Preconditions.checkNotNull(mold, "Casting Recipe mold may not be null.");
		this.output = Preconditions.checkNotNull(output, "Casting Recipe output may not be null.");
		Preconditions.checkArgument(speed > 0, "Casting Recipe speed must be positive.");
		this.speed = speed;
	}

	@Override
	public FluidStack getInput() {
		return input;
	}

	@Override
	public Ingredient getItemInput() {
		return item;
	}

	@Override
	public Ingredient getMold() {
		return mold;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public int getCastingSpeed() {
		return speed;
	}
}
