package exter.foundry.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import exter.foundry.config.FoundryConfig;
import net.minecraftforge.fml.common.Loader;

public final class ModIntegrationManager {

	public static final String BOTANIA = "botania";
	public static final String ENDERIO = "enderio";
	public static final String TCONSTRUCT = "tconstruct";
	public static final String CRAFTTWEAKER = "crafttweaker";

	private static final List<IModIntegration> INTEGRATIONS = new ArrayList<>();

	public static void register(IModIntegration integration) {
		String modid = integration.getModID();
		if (FoundryConfig.config.getBoolean("Integration: " + modid, "Integrations", true, "Toggle for integration with " + modid)) INTEGRATIONS.add(integration);
	}

	/**
	 * Applies an action to each integration.
	 * @param action The action to perform.
	 */
	public static void apply(Consumer<IModIntegration> action) {
		INTEGRATIONS.forEach(action);
	}

	/**
	 * Registers the default integrations.
	 */
	public static void registerDefaults() {
		ModIntegrationManager.register(new ModIntegrationMolten());
		if (Loader.isModLoaded(BOTANIA)) ModIntegrationManager.register(new ModIntegrationBotania());
		if (Loader.isModLoaded(ENDERIO)) ModIntegrationManager.register(new ModIntegrationEnderIO());
		if (Loader.isModLoaded(TCONSTRUCT)) ModIntegrationManager.register(new ModIntegrationTiCon());
		if (Loader.isModLoaded(CRAFTTWEAKER)) ModIntegrationManager.register(new ModIntegrationMinetweaker()); //Must be last.
	}

}
