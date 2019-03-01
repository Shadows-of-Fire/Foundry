package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundry extends TileEntity implements ITickable, IInventory {
	/**
	 * Links an item slot to a tank for filling/draining containers.
	 */
	public class ContainerSlot {
		public final boolean fill;
		public final int tank_slot;
		public final int slot;

		public final Fluid fluid;

		public ContainerSlot(int container_tank, int container_slot, boolean container_fill) {
			this(container_tank, container_slot, container_fill, null);
		}

		public ContainerSlot(int container_tank, int container_slot, boolean container_fill, Fluid container_fluid) {
			tank_slot = container_tank;
			slot = container_slot;
			fill = container_fill;
			fluid = container_fluid;
		}

		public void update() {
			if (container_timer > 0) return;
			ItemStack stack = getStackInSlot(slot);
			if (stack.getCount() > 1) return;

			FluidTank tank = getTank(tank_slot);
			if (fill) {
				if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
					IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					FluidStack drained = tank.drain(Fluid.BUCKET_VOLUME, false);
					if (drained == null || drained.amount == 0 || fluid != null && drained.getFluid() != fluid) { return; }

					int filled = handler.fill(drained, false);
					if (filled == 0) { return; }
					drained.amount = filled;
					drained = tank.drain(filled, true);
					handler.fill(drained, true);
					updateTank(tank_slot);
					updateInventoryItem(slot);
					container_timer = filled / 25;
				}
			} else {
				if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
					IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					FluidStack drained = handler.drain(Fluid.BUCKET_VOLUME, false);
					if (drained == null || drained.amount == 0 || fluid != null && drained.getFluid() != fluid) { return; }

					int filled = tank.fill(drained, false);
					if (filled == 0) { return; }
					drained.amount = filled;
					drained = handler.drain(filled, true);
					tank.fill(drained, true);
					container_timer = filled / 25;
					if (handler instanceof IFluidHandlerItem && handler.getTankProperties()[0].getContents() == null) TileEntityFoundry.this.setStackInSlot(slot, ((IFluidHandlerItem) handler).getContainer());
					updateTank(tank_slot);
					updateInventoryItem(slot);
				}
			}
		}
	}

	protected class FluidHandler implements IFluidHandler {
		private final int fill_tank;
		private final int drain_tank;
		private final IFluidTankProperties[] props;

		public FluidHandler(int fill_tank, int drain_tank) {
			this.fill_tank = fill_tank;
			this.drain_tank = drain_tank;
			props = new IFluidTankProperties[getTankCount()];
			for (int i = 0; i < props.length; i++) {
				props[i] = new FluidTankPropertiesWrapper(getTank(i));
			}
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if (drain_tank < 0) { return null; }
			return drainTank(drain_tank, resource, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if (drain_tank < 0) { return null; }
			return drainTank(drain_tank, maxDrain, doDrain);
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (fill_tank < 0) { return 0; }
			return fillTank(fill_tank, resource, doFill);
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return props;
		}
	}

	public class ItemHandler extends ItemStackHandler {
		protected final int slots;
		protected final ImmutableSet<Integer> insert_slots;
		protected final ImmutableSet<Integer> extract_slots;

		public ItemHandler(int slots, Set<Integer> insert_slots, Set<Integer> extract_slots) {
			super(TileEntityFoundry.this.inventory);
			this.slots = slots;
			this.insert_slots = ImmutableSet.copyOf(insert_slots);
			this.extract_slots = ImmutableSet.copyOf(extract_slots);
		}

		protected boolean canExtract(int slot) {
			return true;
		}

		protected boolean canInsert(int slot, ItemStack stack) {
			return isItemValidForSlot(slot, stack);
		}

		@Override
		public final ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (!extract_slots.contains(slot) || !canExtract(slot)) { return ItemStack.EMPTY; }
			return super.extractItem(slot, amount, simulate);
		}

		@Override
		public int getSlotLimit(int slot) {
			return getInventoryStackLimit();
		}

		@Override
		public final int getSlots() {
			return slots;
		}

		@Override
		public final ItemStack getStackInSlot(int slot) {
			return inventory.get(slot);
		}

		@Override
		public final ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (!insert_slots.contains(slot) || !canInsert(slot, stack)) { return stack; }
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		protected void onContentsChanged(int slot) {
			updateInventoryItem(slot);
			markDirty();
		}
	}

	public enum RedstoneMode {
		RSMODE_IGNORE(0),
		RSMODE_ON(1),
		RSMODE_OFF(2),
		RSMODE_PULSE(3);

		static public RedstoneMode fromID(int num) {
			for (RedstoneMode m : RedstoneMode.values()) {
				if (m.id == num) { return m; }
			}
			return RSMODE_IGNORE;
		}

		public final int id;

		private RedstoneMode(int num) {
			id = num;
		}
	}

	private RedstoneMode mode;

	private final List<ContainerSlot> conatiner_slots;
	private NBTTagCompound update_packet;
	private boolean initialized;

	protected boolean last_redstone_signal;
	protected boolean redstone_signal;
	protected final NonNullList<ItemStack> inventory;

	private int container_timer;

	public TileEntityFoundry() {
		conatiner_slots = new ArrayList<>();
		last_redstone_signal = false;
		redstone_signal = false;
		initialized = false;
		mode = RedstoneMode.RSMODE_IGNORE;
		inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		container_timer = 0;
	}

	protected final void addContainerSlot(ContainerSlot cs) {
		conatiner_slots.add(cs);
	}

	@Override
	public void clear() {

	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (!world.isRemote && player instanceof EntityPlayerMP) {
			NBTTagCompound tag = new NBTTagCompound();
			super.writeToNBT(tag);
			tag.setInteger("rsmode", mode.id);
			sendPacketToPlayer(tag, (EntityPlayerMP) player);
		}
	}

	@Override
	public final ItemStack decrStackSize(int slot, int amount) {
		if (!getStackInSlot(slot).isEmpty()) {
			ItemStack is;

			if (getStackInSlot(slot).getCount() <= amount) {
				is = getStackInSlot(slot);
				setStackInSlot(slot, ItemStack.EMPTY);
				updateInventoryItem(slot);
				markDirty();
				return is;
			} else {
				is = getStackInSlot(slot).splitStack(amount);
				updateInventoryItem(slot);
				markDirty();
				return is;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	protected final FluidStack drainTank(int slot, FluidStack resource, boolean doDrain) {
		FluidTank tank = getTank(slot);
		if (resource.isFluidEqual(tank.getFluid())) {
			FluidStack drained = tank.drain(resource.amount, doDrain);
			if (doDrain && drained != null && drained.amount > 0) {
				updateTank(slot);
				markDirty();
			}
			return drained;
		}
		return null;
	}

	protected final FluidStack drainTank(int slot, int maxDrain, boolean doDrain) {
		FluidTank tank = getTank(slot);
		FluidStack drained = tank.drain(maxDrain, doDrain);
		if (doDrain && drained != null && drained.amount > 0) {
			updateTank(slot);
			markDirty();
		}
		return drained;
	}

	protected final int fillTank(int slot, FluidStack resource, boolean doFill) {
		FluidTank tank = getTank(slot);
		int filled = tank.fill(resource, doFill);
		if (doFill && filled > 0) {
			updateTank(slot);
			markDirty();
		}
		return filled;
	}

	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			IFluidHandler fluid_handler = getFluidHandler(facing);
			if (fluid_handler != null) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidHandler(facing));
			} else {
				return super.getCapability(cap, facing);
			}
		} else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			IItemHandler item_handler = getItemHandler(facing);
			if (item_handler != null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(facing));
			} else {
				return super.getCapability(cap, facing);
			}
		} else {
			return super.getCapability(cap, facing);
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	protected IFluidHandler getFluidHandler(EnumFacing facing) {
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	protected IItemHandler getItemHandler(EnumFacing facing) {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	public RedstoneMode getRedstoneMode() {
		return mode;
	}

	@Override
	public final ItemStack getStackInSlot(int slot) {
		return inventory.get(slot);
	}

	public abstract FluidTank getTank(int slot);

	public abstract int getTankCount();

	@Override
	public final SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new SPacketUpdateTileEntity(getPos(), 0, nbt);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return getFluidHandler(facing) != null;
		} else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return getItemHandler(facing) != null;
		} else {
			return super.hasCapability(cap, facing);
		}
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		initialized = false;
		onChunkUnload();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack s : inventory)
			if (!s.isEmpty()) return false;
		return true;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(getPos()) != this ? false : player.getDistanceSq(getPos()) <= 64.0D;
	}

	@Override
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		if (world.isRemote) {
			readFromNBT(pkt.getNbtCompound());
		}
	}

	protected abstract void onInitialize();

	@Override
	public void openInventory(EntityPlayer player) {
		if (!world.isRemote && player instanceof EntityPlayerMP) {
			NBTTagCompound tag = writeToNBT(null);
			sendPacketToPlayer(tag, (EntityPlayerMP) player);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		int i;
		for (i = 0; i < getTankCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) compound.getTag("Tank_" + String.valueOf(i));
			if (tag != null) {
				FluidTank tank = getTank(i);
				tank.setFluid(null);
				tank.readFromNBT(tag);
			}
		}

		for (i = 0; i < getSizeInventory(); i++) {
			NBTTagCompound tag = (NBTTagCompound) compound.getTag("Item_" + String.valueOf(i));
			if (tag != null) {
				ItemStack stack = ItemStack.EMPTY;
				if (!tag.getBoolean("empty")) {
					stack = new ItemStack(tag);
				}
				inventory.set(i, stack);
			}
		}
		if (compound.hasKey("rsmode")) {
			mode = RedstoneMode.fromID(compound.getInteger("rsmode"));
		}
		if (compound.hasKey("bucket_timer")) {
			container_timer = compound.getInteger("container_timer");
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (!getStackInSlot(slot).isEmpty()) {
			ItemStack is = getStackInSlot(slot);
			setStackInSlot(slot, ItemStack.EMPTY);
			updateInventoryItem(slot);
			markDirty();
			return is;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public final void setInventorySlotContents(int slot, ItemStack stack) {
		setStackInSlot(slot, stack);

		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		updateInventoryItem(slot);
		markDirty();
	}

	public void setRedstoneMode(RedstoneMode new_mode) {
		if (mode != new_mode) {
			mode = new_mode;
			if (world.isRemote) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("rsmode", mode.id);
				sendToServer(tag);
			}
		}
	}

	public void setStackInSlot(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}

	@Override
	public void update() {
		if (!(initialized || isInvalid())) {
			updateRedstone();
			onInitialize();
			initialized = true;
		}

		if (!world.isRemote) {
			if (update_packet == null) {
				update_packet = new NBTTagCompound();
				super.writeToNBT(update_packet);
			}
			for (ContainerSlot cs : conatiner_slots) {
				cs.update();
			}
			if (container_timer > 0) {
				container_timer--;
			}
			updateServer();

			if (update_packet != null) {
				sendPacketToNearbyPlayers(update_packet);
			}
			update_packet = null;
		} else {
			updateClient();
		}
		last_redstone_signal = redstone_signal;
	}

	protected abstract void updateClient();

	protected final void updateInventoryItem(int slot) {
		if (world.isRemote) { return; }
		if (update_packet == null) {
			update_packet = new NBTTagCompound();
			super.writeToNBT(update_packet);
		}
		writeInventoryItemToNBT(update_packet, slot);
	}

	protected final void updateNBTTag(String name, NBTTagCompound compound) {
		if (world.isRemote) { return; }
		if (update_packet == null) {
			update_packet = new NBTTagCompound();
			super.writeToNBT(update_packet);
		}
		update_packet.setTag(name, compound);
	}

	public void updateRedstone() {
		redstone_signal = world.getRedstonePowerFromNeighbors(getPos()) > 0;
	}

	protected abstract void updateServer();

	protected final void updateTank(int slot) {
		if (world.isRemote) { return; }
		if (update_packet == null) {
			update_packet = new NBTTagCompound();
			super.writeToNBT(update_packet);
		}
		writeTankToNBT(update_packet, slot);
	}

	protected final void updateValue(String name, boolean value) {
		if (world.isRemote) { return; }
		if (update_packet == null) {
			update_packet = new NBTTagCompound();
			super.writeToNBT(update_packet);
		}
		update_packet.setBoolean(name, value);
	}

	protected final void updateValue(String name, int value) {
		if (world != null && world.isRemote) { return; }
		if (update_packet == null) {
			update_packet = new NBTTagCompound();
			super.writeToNBT(update_packet);
		}
		update_packet.setInteger(name, value);
	}

	protected final void updateValue(String name, long value) {
		if (world.isRemote) { return; }
		if (update_packet == null) {
			update_packet = new NBTTagCompound();
			super.writeToNBT(update_packet);
		}
		update_packet.setLong(name, value);
	}

	protected final void writeInventoryItemToNBT(NBTTagCompound compound, int slot) {
		ItemStack is = getStackInSlot(slot);
		NBTTagCompound tag = new NBTTagCompound();
		if (is != null) {
			tag.setBoolean("empty", false);
			is.writeToNBT(tag);
		} else {
			tag.setBoolean("empty", true);
		}
		compound.setTag("Item_" + String.valueOf(slot), tag);
	}

	protected final void writeTankToNBT(NBTTagCompound compound, int slot) {
		NBTTagCompound tag = new NBTTagCompound();
		getTank(slot).writeToNBT(tag);
		compound.setTag("Tank_" + String.valueOf(slot), tag);
	}

	protected void writeTileToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (compound == null) {
			compound = new NBTTagCompound();
		}
		int i;
		super.writeToNBT(compound);
		for (i = 0; i < getTankCount(); i++) {
			writeTankToNBT(compound, i);
		}
		for (i = 0; i < getSizeInventory(); i++) {
			writeInventoryItemToNBT(compound, i);
		}
		compound.setInteger("rsmode", mode.id);
		compound.setInteger("container_timer", container_timer);
		return compound;
	}
}
