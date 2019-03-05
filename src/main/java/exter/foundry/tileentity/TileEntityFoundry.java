package exter.foundry.tileentity;

import javax.annotation.Nullable;

import exter.foundry.util.RedstoneMode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundry extends TileEntity implements ITickable {

	protected RedstoneMode mode;
	protected boolean lastRedSignal;
	protected boolean redSignal;
	protected ItemStackHandler inv = new ItemStackHandler(getInvSize());

	@Override
	public void update() {
		lastRedSignal = redSignal;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (getInvSize() > 0) tag.setTag("inv", inv.serializeNBT());
		tag.setByte("rsmode", (byte) mode.ordinal());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (getInvSize() > 0) inv.deserializeNBT(tag.getCompoundTag("inv"));
		mode = RedstoneMode.byID(tag.getByte("rsmode"));
	}

	@Override
	public final SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeSync(tag);
		return new SPacketUpdateTileEntity(pos, 0, tag);
	}

	@Override
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readSync(pkt.getNbtCompound());
	}

	@Override
	public final NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		writeSync(tag);
		return tag;
	}

	@Override
	public final void handleUpdateTag(NBTTagCompound tag) {
		readSync(tag);
	}

	abstract void writeSync(NBTTagCompound tag);

	abstract void readSync(NBTTagCompound tag);

	abstract int getInvSize();

	@Nullable
	public IItemHandler getItemHandler(EnumFacing facing) {
		return getInvSize() > 0 ? inv : null;
	}

	@Nullable
	public IFluidHandler getFluidHandler(EnumFacing facing) {
		return null;
	}

	public RedstoneMode getRedstoneMode() {
		return mode;
	}

	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			IFluidHandler fluid = getFluidHandler(facing);
			if (fluid != null) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluid);
			return super.getCapability(cap, facing);
		}
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			IItemHandler item = getItemHandler(facing);
			if (item != null) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(item);
			return super.getCapability(cap, facing);
		}
		return super.getCapability(cap, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return getFluidHandler(facing) != null;
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return getItemHandler(facing) != null;
		return super.hasCapability(cap, facing);
	}

	@Nullable
	public ItemStackHandler getInv() {
		return inv;
	}
}
