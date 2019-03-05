package exter.foundry.container;

import exter.foundry.container.slot.SlotFurnaceFuel;
import exter.foundry.container.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAlloyFurnace extends Container {

	// Slot numbers
	public static final int SLOTS_TE = 0;

	public static final int SLOTS_TE_SIZE = 4;
	public static final int SLOTS_INVENTORY = 4;

	public static final int SLOTS_HOTBAR = 4 + 3 * 9;
	private static final int SLOT_INVENTORY_X = 8;

	private static final int SLOT_INVENTORY_Y = 84;
	private static final int SLOT_HOTBAR_X = 8;

	private static final int SLOT_HOTBAR_Y = 142;
	private final TileEntityAlloyFurnace furnace;

	public ContainerAlloyFurnace(TileEntityAlloyFurnace furnace, EntityPlayer player) {
		this.furnace = furnace;

		addSlotToContainer(new SlotItemHandler(furnace.getInv(), TileEntityAlloyFurnace.SLOT_INPUT_A, 38, 17));
		addSlotToContainer(new SlotItemHandler(furnace.getInv(), TileEntityAlloyFurnace.SLOT_INPUT_B, 56, 17));
		addSlotToContainer(new SlotOutput(furnace.getInv(), TileEntityAlloyFurnace.SLOT_OUTPUT, 116, 35));
		addSlotToContainer(new SlotFurnaceFuel(furnace.getInv(), TileEntityAlloyFurnace.SLOT_FUEL, 48, 53));

		//Player Inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, SLOT_INVENTORY_X + j * 18, SLOT_INVENTORY_Y + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(player.inventory, i, SLOT_HOTBAR_X + i * 18, SLOT_HOTBAR_Y));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !furnace.isInvalid() && player.getDistanceSq(furnace.getPos()) < 64D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot_index) {
		ItemStack slot_stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slot_index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			slot_stack = stack.copy();

			if (slot_index >= SLOTS_INVENTORY && slot_index <= SLOTS_HOTBAR + 9) {
				if (TileEntityFurnace.isItemFuel(stack)) {
					int s = SLOTS_TE + TileEntityAlloyFurnace.SLOT_FUEL;
					if (!mergeItemStack(stack, s, s + 1, false)) { return ItemStack.EMPTY; }
				}
				if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false)) { return ItemStack.EMPTY; }
			} else if (slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9) {
				if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false)) { return ItemStack.EMPTY; }
			} else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, true)) { return ItemStack.EMPTY; }

			slot.onSlotChanged();
			if (stack.getCount() == slot_stack.getCount()) { return ItemStack.EMPTY; }
			slot.onTake(player, stack);
		}
		return slot_stack;
	}

	int lastBurn;
	int lastFuelBurn;
	int lastProgress;

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (IContainerListener l : listeners) {
			if (furnace.getBurn() != lastBurn) l.sendWindowProperty(this, 0, furnace.getBurn());
			if (furnace.getFuelBurn() != lastFuelBurn) l.sendWindowProperty(this, 1, furnace.getFuelBurn());
			if (furnace.getProgress() != lastProgress) l.sendWindowProperty(this, 2, furnace.getProgress());
		}
		lastBurn = furnace.getBurn();
		lastFuelBurn = furnace.getFuelBurn();
		lastProgress = furnace.getProgress();
	}

	@Override
	public void updateProgressBar(int id, int data) {
		if (id == 0) furnace.setBurn(data);
		else if (id == 1) furnace.setFuelBurn(data);
		else if (id == 2) furnace.setProgress(data);
	}

}