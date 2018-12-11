package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;

public class TileEntityInductionHeater extends TileEntityFoundryPowered {

	private class HeatProvider implements IHeatProvider {
		@Override
		public int provideHeat(int max_heat) {
			if (max_heat > MAX_PROVIDE) {
				max_heat = MAX_PROVIDE;
			}
			if (world.isBlockPowered(pos)) return 0;
			return useFoundryEnergy(max_heat * 3 / 2, true) * 2 / 3;
		}
	}

	private static int MAX_PROVIDE = TileEntityFoundryHeatable.getMaxHeatRecieve(FoundryAPI.CRUCIBLE_ADVANCED_MAX_TEMP, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE);

	private final HeatProvider heat_provider;

	public TileEntityInductionHeater() {
		super();
		heat_provider = new HeatProvider();
	}

	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing) {
		if (cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP) { return FoundryAPI.HEAT_PROVIDER_CAP.cast(heat_provider); }
		return super.getCapability(cap, facing);
	}

	@Override
	public int getFoundryEnergyCapacity() {
		return 25000;
	}

	@Optional.Method(modid = "IC2")
	@Override
	public int getSinkTier() {
		return 2;
	}

	@Override
	public int getSizeInventory() {
		return 0;
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
		return cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP || super.hasCapability(cap, facing);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	@Override
	protected void updateClient() {

	}

	@Override
	protected void updateServer() {
		super.updateServer();
	}
}
