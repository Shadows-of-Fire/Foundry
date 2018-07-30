package exter.foundry.util.hashstack;

import java.util.Map;

import net.minecraft.item.ItemStack;

/**
 * ItemStack wrapper for use in HashMaps (stack size insensitive)
 */
public class HashableItem {
	//Used to get value from a HashMap without creating a new object every time.
	private static final HashableItem cache = new HashableItem();

	//Get a value from a map with HashableItem key.
	public static synchronized <T> T getFromMap(Map<HashableItem, T> map, ItemStack is) {
		cache.setStack(is);
		return map.get(cache);
	}

	protected ItemStack stack;

	private HashableItem() {
		stack = null;
	}

	public HashableItem(ItemStack item_stack) {
		if (item_stack.isEmpty()) {
			stack = ItemStack.EMPTY;
			return;
		}
		stack = item_stack.copy();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (!(obj instanceof HashableItem)) { return false; }
		HashableItem other = (HashableItem) obj;
		return stack.isItemEqual(other.stack) && ItemStack.areItemStackTagsEqual(stack, other.stack);
	}

	public final ItemStack getItemStack() {
		if (stack == null) { return null; }
		return stack.copy();
	}

	@Override
	public int hashCode() {
		if (stack.isEmpty()) { return 0; }
		final int prime = 1289;
		int result = 1;
		result = prime * result + stack.getTranslationKey().hashCode();
		result = prime * result + (stack.getTagCompound() == null ? 0 : stack.getTagCompound().hashCode());
		result = prime * result + stack.getItemDamage();
		return result;
	}

	private void setStack(ItemStack is) {
		stack = is;
	}
}
