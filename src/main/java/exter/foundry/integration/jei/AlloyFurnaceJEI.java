package exter.foundry.integration.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import exter.foundry.Foundry;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class AlloyFurnaceJEI {

	static public class Category implements IRecipeCategory<Wrapper> {

		protected final ResourceLocation background_location;
		@Nonnull
		protected final IDrawableAnimated flame;
		@Nonnull
		protected final IDrawableAnimated arrow;
		@Nonnull
		private final IDrawable background;
		@Nonnull
		private final String localized_name;

		public Category(IJeiHelpers helpers) {
			IGuiHelper guiHelper = helpers.getGuiHelper();
			background_location = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");

			IDrawableStatic flameDrawable = guiHelper.createDrawable(background_location, 176, 0, 14, 14);
			flame = guiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

			IDrawableStatic arrowDrawable = guiHelper.createDrawable(background_location, 176, 14, 24, 17);
			arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

			ResourceLocation location = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");
			background = guiHelper.createDrawable(location, 30, 16, 110, 54);
			localized_name = I18n.format("gui.jei.alloyfurnace");
		}

		@Override
		public void drawExtras(Minecraft minecraft) {
			flame.draw(minecraft, 18, 20);
			arrow.draw(minecraft, 50, 19);
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
			return Foundry.MODID;
		}

		@Nonnull
		@Override
		public String getTitle() {
			return localized_name;
		}

		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			return Collections.emptyList();
		}

		@Nonnull
		@Override
		public String getUid() {
			return FoundryJEIConstants.AF_UID;
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
			IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();

			gui_items.init(0, true, 7, 0);
			gui_items.init(1, true, 25, 0);
			gui_items.init(2, false, 85, 18);

			gui_items.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
			gui_items.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));
			gui_items.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		}
	}

	static public class Wrapper implements IRecipeWrapper {
		@Nonnull
		private final IAlloyFurnaceRecipe recipe;

		public Wrapper(@Nonnull IAlloyFurnaceRecipe recipe) {
			this.recipe = recipe;
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
			ingredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(recipe.getInputA().getItems(), recipe.getInputB().getItems()));
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
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
}
