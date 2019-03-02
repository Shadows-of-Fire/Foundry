package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IBurnerHeaterFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public interface IBurnerHeaterFuelManager {

	public void addFuel(IBurnerHeaterFuel fuel);

	public void addFuel(Ingredient item, int burn, int heat);

	public IBurnerHeaterFuel getFuel(ItemStack item);

	public List<IBurnerHeaterFuel> getFuels();

	public int getHeatNeeded(int heat_loss_rate, int temperature);

	public void removeFuel(IBurnerHeaterFuel fuel);
}
