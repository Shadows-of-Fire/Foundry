package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.ICastingTableRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CastingTableRecipe implements ICastingTableRecipe {

	protected final FluidStack input;
	protected final ItemStack output;
	protected final TableType type;

	public CastingTableRecipe(ItemStack output, FluidStack input, TableType type) {
		this.input = Preconditions.checkNotNull(input, "Casting Table Recipe input may not be null.");
		this.output = Preconditions.checkNotNull(output, "Casting Table Recipe output may not be null.");
		this.type = Preconditions.checkNotNull(type, "Casting Table Recipe type may not be null.");
	}

	@Override
	public FluidStack getInput() {
		return input;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public TableType getTableType() {
		return type;
	}

}
