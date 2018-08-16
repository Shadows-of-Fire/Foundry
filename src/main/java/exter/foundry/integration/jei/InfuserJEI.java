package exter.foundry.integration.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.gui.GuiMetalInfuser;
import exter.foundry.tileentity.TileEntityFoundryPowered;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class InfuserJEI {

	static public class Category implements IRecipeCategory<Wrapper> {

		protected final ResourceLocation background_location;
		@Nonnull
		private final IDrawable background;
		@Nonnull
		private final String localizedName;
		@Nonnull
		private final IDrawable tank_overlay;

		@Nonnull
		protected final IDrawableAnimated arrow;

		public Category(IJeiHelpers helpers) {
			IGuiHelper guiHelper = helpers.getGuiHelper();
			background_location = new ResourceLocation("foundry", "textures/gui/infuser.png");

			IDrawableStatic arrowDrawable = guiHelper.createDrawable(background_location, 176, 53, 24, 17);
			arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

			ResourceLocation location = new ResourceLocation("foundry", "textures/gui/infuser.png");
			background = guiHelper.createDrawable(location, 15, 41, 137, 51);
			tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
			localizedName = I18n.format("gui.jei.infuser");

		}

		@Override
		public void drawExtras(Minecraft minecraft) {
			arrow.draw(minecraft, 34, 18);
		}

		@Override
		@Nonnull
		public IDrawable getBackground() {
			return background;
		}

		@Override
		public IDrawable getIcon() {
			return null;
		}

		@Override
		public String getModName() {
			return Foundry.MODNAME;
		}

		@Nonnull
		@Override
		public String getTitle() {
			return localizedName;
		}

		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			return Collections.emptyList();
		}

		@Nonnull
		@Override
		public String getUid() {
			return FoundryJEIConstants.INF_UID;
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
			IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

			guiFluidStacks.init(0, true, 59, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY, false, tank_overlay);
			guiFluidStacks.init(1, false, 108, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY, false, tank_overlay);
			guiFluidStacks.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
			guiFluidStacks.set(1, ingredients.getOutputs(VanillaTypes.FLUID).get(0));

			IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

			guiItemStacks.init(0, true, 14, 17);
			guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		}
	}

	static public class Wrapper implements IRecipeWrapper {
		private final IInfuserRecipe recipe;

		public Wrapper(IInfuserRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			minecraft.fontRenderer.drawString(recipe.getEnergyNeeded() / TileEntityFoundryPowered.RATIO_FE + " FE", 0, 38, 0);
		}

		@Override
		public boolean equals(Object other) {
			return recipe == other;
		}

		@Override
		public void getIngredients(IIngredients ingredients) {
			ingredients.setInput(VanillaTypes.FLUID, recipe.getInputFluid());
			ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(recipe.getInput().getItems()));
			ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput());
		}

		@Override
		public List<String> getTooltipStrings(int x, int y) {
			return null;
		}

		@Override
		public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			return false;
		}
	}
}
