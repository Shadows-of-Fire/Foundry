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
	 * Called at the end of {@link Foundry#preInit}, just before proxy preInit.
	 */
	default void preInit() {
	}

	/**
	 * Called from proxy preInit, very end.  Client-side only.
	 */
	default void preInitClient() {
	}

	/**
	 * Called at the end of {@link Foundry#init}, just before proxy init.
	 */
	default void init() {
	}

	/**
	 * Called from proxy init, very end.  Client-side only.
	 */
	default void initClient() {
	}

	/**
	 * Called at the start of {@link Foundry#postInit}.
	 */
	default void postInitEarly() {
	}

	/**
	 * Called at the end of {@link Foundry#postInit}, just before proxy postInit.
	 */
	default void postInit() {
	}

	/**
	 * Called from proxy postInit, very end.  Client-side only.
	 */
	default void postInitClient() {
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
