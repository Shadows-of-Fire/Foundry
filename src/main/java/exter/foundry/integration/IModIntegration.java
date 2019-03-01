package exter.foundry.integration;

import exter.foundry.Foundry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Represents an integration.  These need to be registered via {@link ModIntegrationManager#register(IModIntegration)}.
 * @author Shadows
 *
 */
public interface IModIntegration {

	/**
	 * @return The modid of the integrating mod.
	 */
	String getModID();

	/**
	 * Called at the end of {@link Foundry#preInit}.
	 */
	default void preInit() {
	}

	/**
	 * Called at the end of {@link Foundry#init}.
	 */
	default void init() {
	}

	/**
	 * Called at the start of {@link Foundry#postInit}.
	 */
	default void postInitEarly() {
	}

	/**
	 * Called at the end of {@link Foundry#postInit}.
	 */
	default void postInit() {
	}

	/**
	 * Helper method to grab an itemstack with the domain of this integration.
	 */
	default ItemStack getItemStack(String name, int meta) {
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(getModID(), name));
		if (item == null) return ItemStack.EMPTY;
		return new ItemStack(item, 1, meta);
	}

	default ItemStack getItemStack(String name) {
		return getItemStack(name, 0);
	}
}
