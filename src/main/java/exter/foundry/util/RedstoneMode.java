package exter.foundry.util;

import net.minecraft.util.math.MathHelper;

public enum RedstoneMode {
	RSMODE_IGNORE,
	RSMODE_ON,
	RSMODE_OFF,
	RSMODE_PULSE;

	private static RedstoneMode[] VALS = values();

	public static RedstoneMode byID(int id) {
		return VALS[MathHelper.clamp(id, 0, 3)];
	}

}