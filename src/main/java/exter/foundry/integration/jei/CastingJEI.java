package exter.foundry.integration.jei;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.gui.GuiMetalCaster;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import shadows.placebo.util.PlaceboUtil;

public class CastingJEI {

	static public class Category implements IRecipeCategory<Wrapper> {

		protected final ResourceLocation backgroundLocation;

		protected final IDrawableAnimated arrow;

		private final IDrawable background;

		private final String localizedName;

		private final IDrawable tank_overlay;

		public Category(IJeiHelpers helpers) {
			IGuiHelper guiHelper = helpers.getGuiHelper();
			backgroundLocation = new ResourceLocation("foundry", "textures/gui/caster.png");

			IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 53, 24, 17);
			arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

			ResourceLocation location = new ResourceLocation("foundry", "textures/gui/caster.png");
			background = guiHelper.createDrawable(location, 38, 16, 68, 54);
			tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
			localizedName = I18n.format("gui.jei.casting");

		}

		@Override
		public void drawExtras(Minecraft minecraft) {
			arrow.draw(minecraft, 22, 35);
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
			return FoundryJEIConstants.CAST_UID;
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
			IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();
			IGuiFluidStackGroup gui_fluids = recipeLayout.getFluidStacks();

			gui_items.init(0, false, 47, 34);
			gui_items.init(1, true, 27, 4);
			gui_items.init(2, true, 47, 4);
			gui_fluids.init(3, true, 1, 5, 16, GuiMetalCaster.TANK_HEIGHT, FoundryAPI.CASTER_TANK_CAPACITY, false, tank_overlay);
			gui_items.set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
			gui_items.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(0));
			gui_items.set(2, ingredients.getInputs(VanillaTypes.ITEM).get(1));
			gui_fluids.set(3, ingredients.getInputs(VanillaTypes.FLUID).get(0));
		}
	}

	static public class Wrapper implements IRecipeWrapper {

		private final ICastingRecipe recipe;

		public Wrapper(ICastingRecipe recipe) {
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
			List<ItemStack> item = PlaceboUtil.asList(recipe.getItemInput().getMatchingStacks());
			ingredients.setInputs(VanillaTypes.FLUID, Collections.singletonList(recipe.getInput()));
			ingredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(PlaceboUtil.asList(recipe.getMold().getMatchingStacks()), item));
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
