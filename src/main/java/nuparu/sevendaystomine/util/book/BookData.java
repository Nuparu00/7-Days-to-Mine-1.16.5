package nuparu.sevendaystomine.util.book;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.Utils;

public class BookData {

	private List<Page> pages;
	private String title;
	private String desc;

	public BookData(List<Page> pages) {
		this.pages = pages;
	}

	public BookData(List<Page> pages, String title, String desc) {
		this.pages = pages;
		this.title = title;
		this.desc = desc;
	}

	public List<Page> getPages(){
		return this.pages;
	}

	public String getTitle(){
		return title;
	}

	public String getDesc(){
		return desc;
	}
	public static class Page {
		public ResourceLocation res;
		public List<TextBlock> textBlocks;
		public List<Image> images;
		public List<CraftingMatrix> crafting;
		public List<Stack> stacks;

		public Page(ResourceLocation res, List<TextBlock> texts, List<Image> images, List<CraftingMatrix> crafting,
				List<Stack> stacks) {
			this.res = res;
			this.textBlocks = texts;
			this.images = images;
			this.crafting = crafting;
			this.stacks = stacks;
		}

	}

	public static class TextBlock {
		public double x;
		public double y;
		public double z;

		public double width;
		public double height;
		public String text;

		public boolean unlocalized;
		public boolean centered;
		public boolean shadow;
		public int color;
		public int hoverColor = -1;
		public double scale = 1;
		public int link = -1;

		public TextFormatting[] formatting;

		public TextBlock(int x, int y, int z, double width, double height, String text) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height = height;
			this.text = text;

		}
	}

	public static class Image {
		public double x;
		public double y;
		public double z;

		public double width;
		public double height;

		public ResourceLocation res;

		public Image(int x, int y, int z, double width, double height, ResourceLocation res) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height = height;
			this.res = res;

		}
	}

	public static class CraftingMatrix {
		public int x;
		public int y;
		public int z;

		public ResourceLocation res;
		public static final ResourceLocation BACKGROUND_3X3 = new ResourceLocation(SevenDaysToMine.MODID,
				"textures/gui/books/crafting_background.png");
		public static final ResourceLocation BACKGROUND_5X5 = new ResourceLocation(SevenDaysToMine.MODID,
				"textures/gui/books/workbench_background.png");

		boolean workbench;

		boolean init = false;

		public CraftingMatrix(int x, int y, int z, ResourceLocation res) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.res = res;

		}

		private IRecipe recipe;
		private final List<GhostIngredient> ingredients = Lists.<GhostIngredient>newArrayList();
		private float time;

		public void clear() {
			this.recipe = null;
			this.ingredients.clear();
			this.time = 0.0F;
		}

		public void addIngredient(Ingredient p_194187_1_, int p_194187_2_, int p_194187_3_) {
			this.ingredients.add(new GhostIngredient(p_194187_1_, p_194187_2_, p_194187_3_));
		}

		public GhostIngredient get(int p_192681_1_) {
			return this.ingredients.get(p_192681_1_);
		}

		public int size() {
			return this.ingredients.size();
		}

		@Nullable
		public IRecipe getRecipe() {
			return this.recipe;
		}

		public void setRecipe(IRecipe p_192685_1_) {
			this.recipe = p_192685_1_;
		}

		public void loadRecipe(MinecraftServer server) {
			Optional<? extends IRecipe<?>> optional = server.getRecipeManager().byKey(res);
			if (optional.isPresent()) {
				IRecipe rec = optional.get();
				if (rec != null) {
					setRecipe(rec);

					Iterator<Ingredient> iterator = rec.getIngredients().iterator();
					int i = 3;
					int j = rec instanceof net.minecraftforge.common.crafting.IShapedRecipe
							? Math.max(3, ((net.minecraftforge.common.crafting.IShapedRecipe) rec).getRecipeHeight())
							: i;
					int k = rec instanceof net.minecraftforge.common.crafting.IShapedRecipe
							? ((net.minecraftforge.common.crafting.IShapedRecipe) rec).getRecipeWidth()
							: i;
					int l = 1;
					workbench = j > 3 || k > 3;

					addIngredient(Ingredient.of(rec.getResultItem()), workbench ? 175 : 85,
							(int) (1 + 18 * (Math.ceil((workbench ? 5 : 3) / 2f) - 1)));

					for (int i1 = 0; i1 < j; ++i1) {
						for (int j1 = 0; j1 < k; ++j1) {
							if (!iterator.hasNext()) {
								return;
							}

							Ingredient ingredient = iterator.next();

							if (ingredient.getItems().length > 0) {
								addIngredient(ingredient, 1 + j1 * 18, 1 + i1 * 18);
							}

							++l;
						}

						if (k < i) {
							l += i - k;
						}
					}
				}
			}
			else {
				SevenDaysToMine.LOGGER.error("Recipe not found " + res.toString());
			}
		}

		@OnlyIn(Dist.CLIENT)
		public void render(MatrixStack matrix, Minecraft p_194188_1_, int p_194188_2_, int p_194188_3_,
				boolean p_194188_4_, float p_194188_5_) {
			RenderSystem.pushMatrix();
			RenderSystem.enableAlphaTest();
			RenderUtils.drawTexturedRect(matrix, workbench ? BACKGROUND_5X5 : BACKGROUND_3X3, p_194188_2_, p_194188_3_,
					0, 0, workbench ? 196 : 106, workbench ? 90 : 54, workbench ? 196 : 106, workbench ? 90 : 54, 1, 0);



			if (!Screen.hasShiftDown()) {
				this.time += p_194188_5_;
			}

			for (int i = 0; i < this.ingredients.size(); ++i) {
				GhostIngredient ghostrecipe$ghostingredient = this.ingredients.get(i);
				int j = ghostrecipe$ghostingredient.getX() + p_194188_2_;
				int k = ghostrecipe$ghostingredient.getY() + p_194188_3_;

				ItemStack itemstack = ghostrecipe$ghostingredient.getItem();
				ItemRenderer renderitem = p_194188_1_.getItemRenderer();
				renderitem.renderGuiItem(itemstack, j, k);

				if (i == 0) {
					renderitem.renderGuiItemDecorations(p_194188_1_.font, itemstack, j, k);
				}

			}

			RenderSystem.popMatrix();
		}

		@OnlyIn(Dist.CLIENT)
		public class GhostIngredient {
			private final Ingredient ingredient;
			private final int x;
			private final int y;

			public GhostIngredient(Ingredient p_i47604_2_, int p_i47604_3_, int p_i47604_4_) {
				this.ingredient = p_i47604_2_;
				this.x = p_i47604_3_;
				this.y = p_i47604_4_;
			}

			public int getX() {
				return this.x;
			}

			public int getY() {
				return this.y;
			}

			public ItemStack getItem() {
				ItemStack[] aitemstack = this.ingredient.getItems();
				return aitemstack[MathHelper.floor(CraftingMatrix.this.time / 30.0F) % aitemstack.length];
			}
		}

	}

	public static class Stack {
		public int x;
		public int y;
		public int z;

		public ItemStack stack = ItemStack.EMPTY;

		public Stack(int x, int y, int z, ItemStack stack) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.stack = stack;

		}

		@OnlyIn(Dist.CLIENT)
		public void render(Minecraft mc, int x, int y, float partialTicks) {
			RenderSystem.pushMatrix();
			RenderSystem.translated(0, 0, z);
			ItemRenderer renderitem = mc.getItemRenderer();
			renderitem.renderGuiItem(stack, x, y);

			renderitem.renderGuiItemDecorations(mc.font, stack, x, y);
			RenderSystem.translated(0, 0, -z);
			RenderSystem.popMatrix();
		}
	}

}
