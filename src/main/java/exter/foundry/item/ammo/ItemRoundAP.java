package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.Foundry;
import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundAP extends Item {
	static private class FirearmRound extends FirearmRoundBase {
		public FirearmRound() {
			super(7, 50, 25);
		}

		@Override
		public boolean ignoresArmor() {
			return true;
		}
	}

	public ItemRoundAP() {
		setCreativeTab(FoundryTabFirearms.INSTANCE);
		setTranslationKey(Foundry.MODID + ".roundAP");
		setRegistryName(Foundry.MODID, "roundAP");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4) {
		super.addInformation(stack, player, list, par4);
		if (GuiScreen.isShiftKeyDown()) {
			list.add(TextFormatting.BLUE + "Base Damage: 7");
			list.add(TextFormatting.BLUE + "Base Range: 50");
			list.add(TextFormatting.BLUE + "Falloff Range: 25");
			list.add(TextFormatting.YELLOW + "Bypasses armor.");
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FirearmRound();
	}
}
