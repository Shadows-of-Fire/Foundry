package exter.foundry.integration.jei;

import java.util.Collections;
import java.util.List;

import exter.foundry.Foundry;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.FoundryBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class CastingTableJEI {

	public class Category implements IRecipeCategory<Wrapper> {

		protected final ResourceLocation backgroundLocation;

		private final IDrawable background;

		private final String localizedName;

		private final IJeiHelpers helpers;

		public Category(IJeiHelpers helpers) {
			this.helpers = helpers;
			IGuiHelper guiHelper = helpers.getGuiHelper();
			backgroundLocation = new ResourceLocation("foundry", "textures/gui/casting_table_jei.png");

			ResourceLocation location = new ResourceLocation("foundry", "textures/gui/casting_table_jei.png");
			background = guiHelper.createDrawable(location, 0, 0, 74, 59);
			localizedName = I18n.format("gui.jei.casting_table." + name);
		}

		@Override
		public void drawExtras(Minecraft minecraft) {

		}

		@Override

		public IDrawable getBackground() {
			return background;
		}

		@Override
		public IDrawable getIcon() {
			return null;
		}

		@Override
		public String getModName() {
			return Foundry.MODID;
		}

		@Override
		public String getTitle() {
			return localizedName;
		}

		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			return Collections.emptyList();
		}

		@Override
		public String getUid() {
			return UID;
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
			IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();
			IGuiFluidStackGroup gui_fluids = recipeLayout.getFluidStacks();
			IStackHelper stack_helper = helpers.getStackHelper();

			List<FluidStack> input = ingredients.getInputs(VanillaTypes.FLUID).get(0);

			gui_items.init(0, false, 53, 20);
			gui_items.init(1, true, 3, 39);
			gui_fluids.init(2, true, 4, 4, 16, 24, input.get(0).amount, false, null);

			gui_items.set(0, stack_helper.toItemStackList(ingredients.getOutputs(VanillaTypes.ITEM).get(0)));
			gui_items.set(1, table_item);
			gui_fluids.set(2, input.get(0));
		}
	}

	public static class Wrapper implements IRecipeWrapper {
		private final ICastingTableRecipe recipe;
		private final String name;

		public Wrapper(ICastingTableRecipe recipe) {
			this.recipe = recipe;
			name = recipe.getTableType().name().toLowerCase();
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		}

		@Override
		public boolean equals(Object other) {
			return recipe == other;
		}

		@Override
		public void getIngredients(IIngredients ingredients) {
			ingredients.setInput(VanillaTypes.FLUID, recipe.getInput());
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
		}

		public String getName() {
			return name;
		}

		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			return null;
		}

		@Override
		public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			return false;
		}
	}

	private final ItemStack table_item;
	private final String name;

	private final BlockCastingTable.EnumTable type;

	private final String UID;

	public CastingTableJEI(BlockCastingTable.EnumTable table) {
		type = table;
		table_item = FoundryBlocks.block_casting_table.asItemStack(table);
		name = table.name;
		UID = "foundry.casting_table." + name;
	}

	public BlockCastingTable.EnumTable getType() {
		return type;
	}

	public String getUID() {
		return UID;
	}

}
