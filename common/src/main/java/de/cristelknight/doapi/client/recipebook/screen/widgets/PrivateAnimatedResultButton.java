package de.cristelknight.doapi.client.recipebook.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import de.cristelknight.doapi.client.recipebook.handler.AbstractPrivateRecipeScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.joml.Matrix4fStack;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PrivateAnimatedResultButton extends AbstractWidget {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/sprites/recipe_book/slot_craftable.png");

    public static final ResourceLocation SLOT_CRAFTABLE_SPRITE = ResourceLocation.withDefaultNamespace("recipe_book/slot_craftable");
    public static final ResourceLocation SLOT_UNCRAFTABLE_SPRITE = ResourceLocation.withDefaultNamespace("recipe_book/slot_uncraftable");

    private AbstractPrivateRecipeScreenHandler craftingScreenHandler;
    private Recipe<?> recipe;
    private float bounce;

    public PrivateAnimatedResultButton() {
        super(0, 0, 25, 25, CommonComponents.EMPTY);
    }

    public void showResultCollection(Recipe<?> recipe, AbstractPrivateRecipeScreenHandler craftingScreenHandler) {
        this.recipe = recipe;
        this.craftingScreenHandler = craftingScreenHandler;
    }

    public Recipe<?> getRecipe() {
        return this.recipe;
    }

    public void setPos(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        Minecraft minecraftClient = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        ResourceLocation background = SLOT_CRAFTABLE_SPRITE;

        if (!craftingScreenHandler.hasIngredient(this.getRecipe())) {
            background = SLOT_UNCRAFTABLE_SPRITE;
        }

        boolean bl = this.bounce > 0.0F;
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        if (bl) {
            float f = 1.0F + 0.1F * (float) Math.sin((this.bounce / 15.0F * 3.1415927F));
            matrix4fStack.pushMatrix();
            matrix4fStack.translate((this.getX() + 8), (this.getY() + 12), 0.0F);
            matrix4fStack.scale(f, f, 1.0F);
            matrix4fStack.translate((-(this.getX() + 8)), (-(this.getY() + 12)), 0.0F);
            RenderSystem.applyModelViewMatrix();
            this.bounce -= delta;
        }

        guiGraphics.blitSprite(background, getX(), getY(), this.width, this.height);
        Recipe<?> recipe = this.getResult();
        int k = 4;

        guiGraphics.renderItem(recipe.getResultItem(minecraftClient.level.registryAccess()), this.getX() + k, this.getY() + k);
        if (bl) {
            matrix4fStack.popMatrix();
            RenderSystem.applyModelViewMatrix();
        }

    }

    private Recipe<?> getResult() {
        return this.recipe;
    }

    public boolean hasResult() {
        return this.getResult() != null;
    }

    public Recipe<?> currentRecipe() {
        return this.getResult();
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    public List<Component> getOutputTooltip() {
        ItemStack itemStack = this.currentRecipe().getResultItem(Minecraft.getInstance().level.registryAccess());
        return List.of(itemStack.getHoverName());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        ItemStack itemStack = this.currentRecipe().getResultItem(Minecraft.getInstance().level.registryAccess());
        builder.add(NarratedElementType.TITLE, Component.translatable("narration.recipe", itemStack.getHoverName()));

        builder.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
    }

    @Override
    public int getWidth() {
        return 25;
    }
}