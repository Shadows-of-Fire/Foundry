package exter.foundry.recipes;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.IBurnerHeaterFuel;
import net.minecraft.item.crafting.Ingredient;

public class BurnerHeaterFuel implements IBurnerHeaterFuel {

	protected final Ingredient fuel;
	protected final int burnTime;
	protected final int heat;

	public BurnerHeaterFuel(Ingredient fuel, int burnTime, int heat) {
		this.fuel = Preconditions.checkNotNull(fuel, "Burner Heater Fuel input may not be null.");
		Preconditions.checkArgument(burnTime > 0, "Burner Heater Fuel time must be positive.");
		Preconditions.checkArgument(heat > 0, "Burner Heater Fuel heat must be positive.");
		this.burnTime = burnTime;
		this.heat = heat;
	}

	@Override
	public Ingredient getFuel() {
		return fuel;
	}

	@Override
	public int getBurnTime() {
		return burnTime;
	}

	@Override
	public int getHeat() {
		return heat;
	}

}
