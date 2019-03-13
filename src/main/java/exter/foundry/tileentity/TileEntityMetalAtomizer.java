package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityMetalAtomizer extends TileEntityFoundryPowered {

	public static final int ATOMIZE_TIME = 500000;
	public static final int ENERGY_REQUIRED = 15000;
	public static final int INVENTORY_OUTPUT = 0;
	public static final int INVENTORY_CONTAINER_DRAIN = 1;
	public static final int INVENTORY_CONTAINER_FILL = 2;
	public static final int INVENTORY_CONTAINER_WATER_DRAIN = 3;
	public static final int INVENTORY_CONTAINER_WATER_FILL = 4;

	protected final FluidTank inputTank = new FluidTank(FoundryAPI.ATOMIZER_TANK_CAPACITY);
	protected final FluidTank waterTank = new FluidTank(FoundryAPI.ATOMIZER_WATER_TANK_CAPACITY);

	protected IAtomizerRecipe recipe;
	protected int progress;

	private final FluidStack water_required = new FluidStack(FluidRegistry.WATER, 50);

	public TileEntityMetalAtomizer() {

		current_recipe = null;

		addContainerSlot(new ContainerSlot(TANK_INPUT, INVENTORY_CONTAINER_DRAIN, false));
		addContainerSlot(new ContainerSlot(TANK_INPUT, INVENTORY_CONTAINER_FILL, true));
		addContainerSlot(new ContainerSlot(TANK_WATER, INVENTORY_CONTAINER_WATER_DRAIN, false, FluidRegistry.WATER));
		addContainerSlot(new ContainerSlot(TANK_WATER, INVENTORY_CONTAINER_WATER_FILL, true, FluidRegistry.WATER));

		update_energy = true;
	}

	private void beginAtomizing() {
		if (current_recipe != null && canAtomizeCurrentRecipe() && getStoredFoundryEnergy() >= ENERGY_REQUIRED) {
			useFoundryEnergy(ENERGY_REQUIRED, true);
			progress = 0;
		}
	}

	private boolean canAtomizeCurrentRecipe() {
		if (tanks[TANK_WATER].getFluid() == null || !tanks[TANK_WATER].getFluid().containsFluid(water_required)) { return false; }

		ItemStack recipe_output = current_recipe.getOutput();

		ItemStack inv_output = inventory.get(INVENTORY_OUTPUT);
		if (!inv_output.isEmpty() && (!inv_output.isItemEqual(recipe_output) || inv_output.getCount() + recipe_output.getCount() > inv_output.getMaxStackSize())) { return false; }
		return true;
	}

	private void checkCurrentRecipe() {
		if (current_recipe == null) {
			progress = -1;
			return;
		}

		if (!current_recipe.matchesRecipe(tanks[TANK_INPUT].getFluid())) {
			progress = -1;
			current_recipe = null;
			return;
		}
	}

	@Override
	protected IFluidHandler getFluidHandler(EnumFacing facing) {
		return fluid_handler;
	}

	@Override
	public int getFoundryEnergyCapacity() {
		return 60000;
	}

	@Override
	protected IItemHandler getItemHandler(EnumFacing side) {
		return item_handler;
	}

	public int getProgress() {
		return progress;
	}

	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public FluidTank getTank(int slot) {
		if (slot < 0 || slot > 1) { return null; }
		return tanks[slot];
	}

	@Override
	public int getTankCount() {
		return 2;
	}

	@Deprecated
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound compund) {
		super.readFromNBT(compund);

		if (compund.hasKey("progress")) {
			progress = compund.getInteger("progress");
		}
	}

	@Override
	protected void updateClient() {

	}

	@Override
	protected void updateServer() {
		super.updateServer();
		int last_progress = progress;

		checkCurrentRecipe();

		if (current_recipe == null) {
			current_recipe = AtomizerRecipeManager.INSTANCE.findRecipe(tanks[TANK_INPUT].getFluid());
			progress = -1;
		}

		if (progress < 0) {
			switch (getRedstoneMode()) {
			case RSMODE_IGNORE:
				beginAtomizing();
				break;
			case RSMODE_OFF:
				if (!redstone_signal) {
					beginAtomizing();
				}
				break;
			case RSMODE_ON:
				if (redstone_signal) {
					beginAtomizing();
				}
				break;
			case RSMODE_PULSE:
				if (redstone_signal && !last_redstone_signal) {
					beginAtomizing();
				}
				break;
			}
		} else {
			if (canAtomizeCurrentRecipe()) {
				FluidStack input_fluid = current_recipe.getInput();
				int increment = 1800000 / input_fluid.amount;
				if (increment > ATOMIZE_TIME / 4) {
					increment = ATOMIZE_TIME / 4;
				}
				if (increment < 1) {
					increment = 1;
				}
				progress += increment;

				if (progress >= ATOMIZE_TIME) {
					progress = -1;
					tanks[TANK_INPUT].drain(input_fluid.amount, true);
					tanks[TANK_WATER].drain(water_required.amount, true);
					if (inventory.get(INVENTORY_OUTPUT).isEmpty()) {
						inventory.set(INVENTORY_OUTPUT, current_recipe.getOutput());
					} else {
						inventory.get(INVENTORY_OUTPUT).grow(current_recipe.getOutput().getCount());
					}
					updateInventoryItem(INVENTORY_OUTPUT);
					updateTank(0);
					updateTank(1);
					markDirty();
				}
			} else {
				progress = -1;
			}
		}

		if (last_progress != progress) {
			updateValue("progress", progress);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (compound == null) {
			compound = new NBTTagCompound();
		}
		super.writeToNBT(compound);
		compound.setInteger("progress", progress);
		return compound;
	}
}
