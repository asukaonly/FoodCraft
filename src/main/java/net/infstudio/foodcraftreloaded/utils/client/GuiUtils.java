package net.infstudio.foodcraftreloaded.utils.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public interface GuiUtils {
    /** Renders the given texture tiled into a GUI */
    static void renderTiledTextureAtlas(int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        putTiledTextureQuads(worldrenderer, x, y, width, height, depth, sprite);

        tessellator.draw();
    }

    static void renderTiledFluid(int x, int y, int width, int height, float depth, FluidStack fluidStack) {
        TextureAtlasSprite fluidSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
        setColorRGBA(fluidStack.getFluid().getColor(fluidStack));
        renderTiledTextureAtlas(x, y, width, height, depth, fluidSprite);
    }

    static void setColorRGB(int color) {
        setColorRGBA(color | 0xff000000);
    }

    static void setColorRGBA(int color) {
        float a = (float) alpha(color) / 255.0F;
        float r = (float) red(color) / 255.0F;
        float g = (float) green(color) / 255.0F;
        float b = (float) blue(color) / 255.0F;

        GlStateManager.color(r, g, b, a);
    }

    static int alpha(int c) {
        return (c >> 24) & 0xFF;
    }

    static int red(int c) {
        return (c >> 16) & 0xFF;
    }

    static int green(int c) {
        return (c >> 8) & 0xFF;
    }

    static int blue(int c) {
        return (c) & 0xFF;
    }

    /** Adds a quad to the rendering pipeline. Call startDrawingQuads beforehand. You need to call draw() yourself. */
    static void putTiledTextureQuads(VertexBuffer renderer, int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();

        // tile vertically
        do {
            int renderHeight = Math.min(sprite.getIconHeight(), height);
            height -= renderHeight;

            float v2 = sprite.getInterpolatedV((16f * renderHeight) / (float) sprite.getIconHeight());

            // we need to draw the quads per width too
            int x2 = x;
            int width2 = width;
            // tile horizontally
            do {
                int renderWidth = Math.min(sprite.getIconWidth(), width2);
                width2 -= renderWidth;

                float u2 = sprite.getInterpolatedU((16f * renderWidth) / (float) sprite.getIconWidth());

                renderer.pos(x2, y, depth).tex(u1, v1).endVertex();
                renderer.pos(x2, y + renderHeight, depth).tex(u1, v2).endVertex();
                renderer.pos(x2 + renderWidth, y + renderHeight, depth).tex(u2, v2).endVertex();
                renderer.pos(x2 + renderWidth, y, depth).tex(u2, v1).endVertex();

                x2 += renderWidth;
            } while(width2 > 0);

            y += renderHeight;
        } while(height > 0);
    }
}
