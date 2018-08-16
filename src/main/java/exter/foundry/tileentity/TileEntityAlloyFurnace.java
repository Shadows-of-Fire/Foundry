package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.block.BlockAlloyFurnace;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.tileentity.itemhandler.ItemHandlerFuel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.item.IExoflameHeatable;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = ModIntegrationManager.BOTANIA)
public class TileEntityAlloyFurnace extends TileEntityFoundry implements ISidedInventory, IExoflameHeatable {
	public static final int SLOT_INPUT_A = 0;
	public static final int SLOT_INPUT_B = 1;
	public static final int SLOT_OUTPUT = 2;
	public static final int SLOT_FUEL = 3;

	@Deprecated
	static private final int[] SLOTS_TOP = new int[] { SLOT_INPUT_A, SLOT_INPUT_B };

	@Deprecated
	static private final int[] SLOTS_BOTTOM = new int[] { SLOT_OUTPUT, SLOT_FUEL };

	@Deprecated
	static private final int[] SLOTS_SIDES = new int[] { SLOT_FUEL };

	static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(SLOT_INPUT_A, SLOT_INPUT_B);

	static private final Set<Integer> IH_SLOTS_INPUT_FUEL = ImmutableSet.of(SLOT_INPUT_A, SLOT_INPUT_B, SLOT_FUEL);
	static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of(SLOT_OUTPUT);
	static private final Set<Integer> IH_SLOTS_OUTPUT_FUEL = ImmutableSet.of(SLOT_OUTPUT, SLOT_FUEL);

	static private final Set<Integer> IH_SLOTS_FUEL = ImmutableSet.of(SLOT_FUEL);
	public int burn_time;
	public int item_burn_time;
	public int progress;
	private boolean update_burn_times;

	private final ItemHandler item_handler;
	private final ItemHandlerFuel item_handler_fuel;

	public TileEntityAlloyFurnace() {
		burn_time = 0;
		item_burn_time = 0;
		progress = 0;
		update_burn_times = false;
		item_handler = new ItemHandler(getSizeInventory(), IH_SLOTS_INPUT, IH_SLOTS_OUTPUT);
		item_handler_fuel = new ItemHandlerFuel(this, getSizeInventory(), IH_SLOTS_INPUT_FUEL, IH_SLOTS_OUTPUT_FUEL, IH_SLOTS_FUEL);
	}

	@Override
	public void boostBurnTime() {
		if (!world.isRemote) {
			burn_time = 200;
			item_burn_time = 199;
			update_burn_times = true;
			markDirty();
		}
	}

	@Override
	public void boostCookTime() {
		progress += 3;
		markDirty();
	}

	@Override
	public boolean canSmelt() {
		IAlloyFurnaceRecipe recipe = null;
		if (!getStackInSlot(SLOT_INPUT_A).isEmpty() && !getStackInSlot(SLOT_INPUT_B).isEmpty()) {
			recipe = AlloyFurnaceRecipeManager.INSTANCE.findRecipe(getStackInSlot(SLOT_INPUT_A), getStackInSlot(SLOT_INPUT_B));
			if (recipe == null) recipe = AlloyFurnaceRecipeManager.INSTANCE.findRecipe(getStackInSlot(SLOT_INPUT_B), getStackInSlot(SLOT_INPUT_A));
		}
		ItemStack output = getStackInSlot(SLOT_OUTPUT);
		return recipe != null && (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(output, recipe.getOutput()) && output.getCount() + recipe.getOutput().getCount() <= output.getMaxStackSize()));
	}

	@Override
	public int getBurnTime() {
		return burn_time <= 1 ? 0 : burn_time - 1;
	}

	@Deprecated
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return side != EnumFacing.UP || slot != SLOT_INPUT_A || slot != SLOT_INPUT_B || stack.getItem() == Items.BUCKET;
	}

	@Deprecated
	@Override
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, EnumFacing side) {
		return isItemValidForSlot(par1, par2ItemStack);
	}

	private boolean canOutput(IAlloyFurnaceRecipe recipe) {
		ItemStack output = getStackInSlot(SLOT_OUTPUT);
		return recipe != null && (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(output, recipe.getOutput()) && output.getCount() + recipe.getOutput().getCount() <= output.getMaxStackSize()));
	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	private void doSmelt(IAlloyFurnaceRecipe recipe, boolean reversed) {
		ItemStack output = recipe.getOutput();
		if (!canOutput(recipe)) {
			progress = 0;
			return;
		}

		if (++progress >= 400) {
			progress = 0;
			if (reversed) {
				decrStackSize(SLOT_INPUT_B, recipe.getInputA().getAmount());
				decrStackSize(SLOT_INPUT_A, recipe.getInputB().getAmount());
			} else {
				decrStackSize(SLOT_INPUT_A, recipe.getInputA().getAmount());
				decrStackSize(SLOT_INPUT_B, recipe.getInputB().getAmount());
			}
			if (getStackInSlot(SLOT_OUTPUT).isEmpty()) {
				setStackInSlot(SLOT_OUTPUT, output.copy());
			} else {
				getStackInSlot(SLOT_OUTPUT).grow(output.getCount());
			}
			updateInventoryItem(SLOT_OUTPUT);
			markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	protected IItemHandler getItemHandler(EnumFacing side) {
		switch (side) {
		case UP:
			return item_handler;
		default:
			return item_handler_fuel;
		}
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Deprecated
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		switch (side) {
		case DOWN:
			return SLOTS_BOTTOM;
		case UP:
			return SLOTS_TOP;
		default:
			return SLOTS_SIDES;
		}
	}

	@Override
	public FluidTank getTank(int slot) {
		return null;
	}

	@Override
	public int getTankCount() {
		return 0;
	}

	public boolean isBurning() {
		return burn_time > 0;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		switch (slot) {
		case SLOT_OUTPUT:
			return false;
		case SLOT_FUEL:
			return TileEntityFurnace.isItemFuel(stack);
		}
		return true;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer) {
		return world.getTileEntity(getPos()) != this ? false : par1EntityPlayer.getDistanceSq(getPos()) <= 64.0D;
	}

	@Override
	protected void onInitialize() {

	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("BurnTime")) {
			burn_time = tag.getInteger("BurnTime");
		}
		if (tag.hasKey("CookTime")) {
			progress = tag.getInteger("CookTime");
		}
		if (tag.hasKey("ItemBurnTime")) {
			item_burn_time = tag.getInteger("ItemBurnTime");
		}
		if (world != null && !world.isRemote) {
			((BlockAlloyFurnace) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	protected void updateClient() {

	}

	@Override
	protected void updateServer() {
		int last_burn_time = burn_time;
		int last_progress = progress;
		int last_item_burn_time = item_burn_time;

		if (burn_time > 0) {
			--burn_time;
		}

		boolean reversed = false;
		IAlloyFurnaceRecipe recipe = null;
		if (!getStackInSlot(SLOT_INPUT_A).isEmpty() && !getStackInSlot(SLOT_INPUT_B).isEmpty()) {
			recipe = AlloyFurnaceRecipeManager.INSTANCE.findRecipe(getStackInSlot(SLOT_INPUT_A), getStackInSlot(SLOT_INPUT_B));
			if (recipe == null) {
				recipe = AlloyFurnaceRecipeManager.INSTANCE.findRecipe(getStackInSlot(SLOT_INPUT_B), getStackInSlot(SLOT_INPUT_A));
				if (recipe != null) {
					reversed = true;
				}
			}
		}

		if (burn_time == 0 && recipe != null && canOutput(recipe)) {
			ItemStack fuel = getStackInSlot(SLOT_FUEL);
			item_burn_time = burn_time = TileEntityFurnace.getItemBurnTime(fuel);
			if (burn_time > 0) {
				if (!fuel.isEmpty()) {
					fuel.shrink(1);
					if (fuel.isEmpty()) {
						setStackInSlot(SLOT_FUEL, fuel.getItem().getContainerItem(fuel));
					}
					updateInventoryItem(SLOT_FUEL);
				}
			}
		}

		if (burn_time > 0) {
			if (recipe != null) {
				doSmelt(recipe, reversed);
			} else {
				progress = 0;
			}
		} else {
			progress = 0;
		}

		if (last_burn_time != burn_time || update_burn_times) {
			if (last_burn_time * burn_time == 0) {
				((BlockAlloyFurnace) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
			}
			updateValue("BurnTime", burn_time);
		}

		if (last_item_burn_time != item_burn_time || update_burn_times) {
			updateValue("ItemBurnTime", item_burn_time);
		}
		update_burn_times = false;

		if (last_progress != progress) {
			updateValue("CookTime", progress);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (compound == null) {
			compound = new NBTTagCompound();
		}
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", burn_time);
		compound.setInteger("CookTime", progress);
		compound.setInteger("ItemBurnTime", item_burn_time);
		return compound;
	}
}
