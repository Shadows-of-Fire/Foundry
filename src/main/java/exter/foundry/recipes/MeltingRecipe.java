package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.IMeltingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class MeltingRecipe implements IMeltingRecipe {

	protected final Ingredient input;
	protected final FluidStack output;
	protected final int temp;
	protected final int speed;

	public MeltingRecipe(FluidStack output, Ingredient input, int temp, int speed) {
		this.input = Preconditions.checkNotNull(input, "Melting Recipe input cannot be null.");
		this.output = Preconditions.checkNotNull(output, "Melting Recipe output cannot be null.");
		Preconditions.checkArgument(temp > 295, "Melting Recipe temp must be greater than 295.");
		Preconditions.checkArgument(speed > 0, "Melting Recipe speed must be positive.");
		this.temp = temp;
		this.speed = speed;
	}

	@Override
	public Ingredient getInput() {
		return input;
	}

	@Override
	public int getMeltingPoint() {
		return temp;
	}

	@Override
	public int getMeltingSpeed() {
		return speed;
	}

	@Override
	public FluidStack getOutput() {
		return output;
	}
}
