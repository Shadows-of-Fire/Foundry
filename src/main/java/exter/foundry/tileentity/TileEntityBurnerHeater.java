package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.block.BlockBurnerHeater;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.tileentity.itemhandler.ItemHandlerFuel;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.IExoflameHeatable;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityBurnerHeater extends TileEntityFoundry implements IExoflameHeatable {
	private class HeatProvider implements IHeatProvider {
		@Override
		public int provideHeat(int max_heat) {
			if (burn_time > 0 && max_heat > 0) {
				if (max_heat > 0) {
					if (max_heat > heat_provide) {
						max_heat = heat_provide;
					}
					int last_burn_time = burn_time;
					burn_time -= FoundryMiscUtils.divCeil(max_heat * 10, heat_provide);
					if (burn_time < 0) {
						burn_time = 0;
					}

					if (last_burn_time != burn_time || update_burn_times) {
						if (last_burn_time * burn_time == 0) {
							((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
						}
						updateValue("BurnTime", burn_time);
					}
				}
				return max_heat;
			}

			return 0;
		}
	}

	private static int DEFAULT_HEAT_PROVIDE = TileEntityFoundryHeatable.getMaxHeatRecieve(FoundryAPI.CRUCIBLE_BASIC_MAX_TEMP, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE);
	static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(0, 1, 2, 3);
	static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of();
	static private final Set<Integer> IH_SLOTS_FUEL = ImmutableSet.of(0, 1, 2, 3);

	private int burn_time;
	private int item_burn_time;

	private int heat_provide;

	private boolean update_burn_times;
	private final HeatProvider heat_provider;
	private final ItemHandlerFuel item_handler;

	public TileEntityBurnerHeater() {
		burn_time = 0;
		item_burn_time = 0;
		update_burn_times = false;
		heat_provide = DEFAULT_HEAT_PROVIDE;
		heat_provider = new HeatProvider();
		item_handler = new ItemHandlerFuel(this, getSizeInventory(), IH_SLOTS_INPUT, IH_SLOTS_OUTPUT, IH_SLOTS_FUEL);
	}

	@Optional.Method(modid = "Botania")
	@Override
	public void boostBurnTime() {
		if (!world.isRemote) {
			heat_provide = DEFAULT_HEAT_PROVIDE;
			burn_time = 2000;
			item_burn_time = 1999;
			update_burn_times = true;
			markDirty();
		}
	}

	@Optional.Method(modid = "Botania")
	@Override
	public void boostCookTime() {

	}

	@Optional.Method(modid = "Botania")
	@Override
	public boolean canSmelt() {
		return true;
	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	public int getBurningTime() {
		return burn_time;
	}

	@Optional.Method(modid = "Botania")
	@Override
	public int getBurnTime() {
		return burn_time <= 1 ? 0 : burn_time - 1;
	}

	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing) {
		if (cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP) { return FoundryAPI.HEAT_PROVIDER_CAP.cast(heat_provider); }
		return super.getCapability(cap, facing);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public int getItemBurnTime() {
		return item_burn_time;
	}

	@Override
	protected IItemHandler getItemHandler(EnumFacing side) {
		return item_handler;
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public FluidTank getTank(int slot) {
		return null;
	}

	@Override
	public int getTankCount() {
		return 0;
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
		return super.hasCapability(cap, facing) || cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP;
	}

	public boolean isBurning() {
		return burn_time > 0;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return TileEntityFurnace.isItemFuel(stack);
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
		if (tag.hasKey("ItemBurnTime")) {
			item_burn_time = tag.getInteger("ItemBurnTime");
		}
		if (tag.hasKey("HeatProvide")) {
			heat_provide = tag.getInteger("HeatProvide");
		}
		if (world != null && !world.isRemote) {
			((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
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
		int last_item_burn_time = item_burn_time;

		if (burn_time < 10) {
			for (int i = 0; i < 4; i++) {
				ItemStack item = getStackInSlot(i);
				if (!item.isEmpty()) {
					int burn = 0;
					IBurnerHeaterFuel fuel = BurnerHeaterFuelManager.INSTANCE.getFuel(item);
					if (fuel != null) {
						burn = fuel.getBurnTime() * 10;
						heat_provide = fuel.getHeat();
					} else {
						burn = TileEntityFurnace.getItemBurnTime(item) * 10;
						heat_provide = DEFAULT_HEAT_PROVIDE;
					}
					if (burn > 0) {
						burn_time += burn;
						item_burn_time = burn_time;
						item.shrink(1);
						if (item.isEmpty()) {
							setStackInSlot(i, item.getItem().getContainerItem(item));
						}
						updateInventoryItem(i);
						break;
					}
				}
			}
		}

		if (last_burn_time != burn_time || update_burn_times) {
			if (last_burn_time * burn_time == 0) {
				((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
			}
			updateValue("BurnTime", burn_time);
		}

		if (last_item_burn_time != item_burn_time || update_burn_times) {
			updateValue("ItemBurnTime", item_burn_time);
		}
		update_burn_times = false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (compound == null) {
			compound = new NBTTagCompound();
		}
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", burn_time);
		compound.setInteger("ItemBurnTime", item_burn_time);
		compound.setInteger("HeatProvide", heat_provide);
		return compound;
	}
}
