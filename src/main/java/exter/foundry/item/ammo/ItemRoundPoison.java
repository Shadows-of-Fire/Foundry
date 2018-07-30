package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.Foundry;
import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundPoison extends Item {
	static private class FirearmRound extends FirearmRoundBase {
		public FirearmRound() {
			super(8, 50, 25);
		}

		@Override
		public void onBulletDamagedLivingEntity(EntityLivingBase entity, int count) {
			entity.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("poison")), 400));
		}

	}

	public ItemRoundPoison() {
		setCreativeTab(FoundryTabFirearms.INSTANCE);
		setTranslationKey(Foundry.MODID + ".roundPoison");
		setRegistryName(Foundry.MODID, "roundPoison");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4) {
		super.addInformation(stack, player, list, par4);
		if (GuiScreen.isShiftKeyDown()) {
			list.add(TextFormatting.BLUE + "Base Damage: 8");
			list.add(TextFormatting.BLUE + "Base Range: 50");
			list.add(TextFormatting.BLUE + "Falloff Range: 25");
			list.add(TextFormatting.YELLOW + "Inflicts poison on target.");
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FirearmRound();
	}
}
