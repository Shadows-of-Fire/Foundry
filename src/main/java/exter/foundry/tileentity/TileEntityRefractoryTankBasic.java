package exter.foundry.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityRefractoryTankBasic extends TileEntityFoundry {

	static public final int SLOT_DRAIN = 0;
	static public final int SLOT_FILL = 1;

	protected final FluidTank tank = new FluidTank(getTankCapacity());;

	@Override
	public IFluidHandler getFluidHandler(EnumFacing facing) {
		return tank;
	}

	@Override
	public int getInvSize() {
		return 2;
	}

	protected int getTankCapacity() {
		return 16000;
	}

	@Override
	void writeSync(NBTTagCompound tag) {
		tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
	}

	@Override
	void readSync(NBTTagCompound tag) {
		tank.readFromNBT(tag.getCompoundTag("tank"));
	}

	public FluidTank getTank() {
		return tank;
	}

}
