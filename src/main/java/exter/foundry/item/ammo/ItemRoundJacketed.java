package exter.foundry.item.ammo;


import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundJacketed extends Item
{
  public ItemRoundJacketed()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundJacketed");
    setRegistryName("roundJacketed");
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list,boolean advanced)
  {
    super.addInformation(stack, player, list, advanced);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 7");
      list.add(TextFormatting.BLUE + "Base Range: 100");
      list.add(TextFormatting.BLUE + "Falloff Range: 40");
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRoundBase(7,100,40);
  }
}
