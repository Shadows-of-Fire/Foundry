package exter.foundry.tileentity;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import vazkii.botania.api.item.IExoflameHeatable;

public class TileEntityAlloyFurnace extends TileEntityFoundry implements IExoflameHeatable {

	public static final int SLOT_INPUT_A = 0;
	public static final int SLOT_INPUT_B = 1;
	public static final int SLOT_OUTPUT = 2;
	public static final int SLOT_FUEL = 3;

	protected int burnTime = 0;
	protected int progress = 0;
	protected int fuelBurnTime = 0;
	protected RangedWrapper fuelSlot = new RangedWrapper(inv, 3, 4);
	protected IAlloyFurnaceRecipe recipe = null;
	protected boolean reversed = false;

	@Override
	public void boostBurnTime() {
		if (!world.isRemote) {
			int oldBurn = burnTime;
			fuelBurnTime = burnTime += 200;
			if (oldBurn <= 0) setBurning(true);
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
		findRecipe();
		return canOutput();
	}

	@Override
	public int getBurnTime() {
		return burnTime;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void update() {
		super.update();
		if (world.isRemote) return;

		if (burnTime > 0) burnTime--;
		else {
			setBurning(false);
			return;
		}

		findRecipe();

		if (burnTime == 0 && recipe != null && canOutput()) burnFuel();

		if (++progress >= 400) {
			if (recipe != null) smeltItem();
			progress = 0;
		}
	}

	protected void findRecipe() {
		ItemStack slotA = inv.getStackInSlot(SLOT_INPUT_A);
		ItemStack slotB = inv.getStackInSlot(SLOT_INPUT_B);

		if (recipe != null) {
			if (!reversed && recipe.matchesRecipe(slotA, slotB)) return;
			if (reversed && recipe.matchesRecipe(slotB, slotA)) return;
		}

		if (!slotA.isEmpty() && !slotB.isEmpty()) {
			recipe = AlloyFurnaceRecipeManager.INSTANCE.findRecipe(slotA, slotB);
			if (recipe == null) {
				recipe = AlloyFurnaceRecipeManager.INSTANCE.findRecipe(slotB, slotA);
				reversed = recipe != null;
			} else reversed = false;
		}
	}

	protected boolean canOutput() {
		ItemStack output = inv.getStackInSlot(SLOT_OUTPUT);
		return recipe != null && (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(output, recipe.getOutput()) && output.getCount() + recipe.getOutput().getCount() <= output.getMaxStackSize()));
	}

	protected void smeltItem() {
		if (!canOutput()) return;

		if (!reversed) {
			inv.getStackInSlot(SLOT_INPUT_A).shrink(recipe.getInputA().getAmount());
			inv.getStackInSlot(SLOT_INPUT_B).shrink(recipe.getInputB().getAmount());
		} else {
			inv.getStackInSlot(SLOT_INPUT_B).shrink(recipe.getInputA().getAmount());
			inv.getStackInSlot(SLOT_INPUT_A).shrink(recipe.getInputB().getAmount());
		}

		if (inv.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
			inv.setStackInSlot(SLOT_OUTPUT, recipe.getOutput().copy());
		} else {
			inv.getStackInSlot(SLOT_OUTPUT).grow(recipe.getOutput().getCount());
		}
		markDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		writeSync(tag);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		readSync(tag);
		if (world != null && !world.isRemote) setBurning(burnTime > 0);
	}

	@Override
	void writeSync(NBTTagCompound tag) {
		tag.setInteger("burnTime", burnTime);
		tag.setInteger("progress", progress);
		tag.setInteger("fuelBurnTime", fuelBurnTime);
	}

	@Override
	void readSync(NBTTagCompound tag) {
		burnTime = tag.getInteger("burnTime");
		progress = tag.getInteger("progress");
		fuelBurnTime = tag.getInteger("fuelBurnTime");
	};
}
