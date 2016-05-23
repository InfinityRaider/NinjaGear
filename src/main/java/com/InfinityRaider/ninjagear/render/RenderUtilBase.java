package com.InfinityRaider.ninjagear.render;

import com.InfinityRaider.ninjagear.handler.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Utility base class for rendering event handlers
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public abstract class RenderUtilBase {
    public static final RenderUtilBase RENDER_UTIL = new RenderUtilBase() {};

    protected RenderUtilBase() {}

    /**
     * Method to cancel out view bobbing when rendering from RenderHandEvent
     * @param player player
     * @param partialTicks partial tick
     * @param inverse inverse or not
     */
    public void correctViewBobbing(EntityPlayer player, float partialTicks, boolean inverse) {
        if (!Minecraft.getMinecraft().gameSettings.viewBobbing) {
            return;
        }
        float f = player.distanceWalkedModified - player.prevDistanceWalkedModified;
        float f1 = -(player.distanceWalkedModified + f * partialTicks);
        float f2 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        float f3 = player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * partialTicks;
        if(inverse) {
            GlStateManager.translate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
            GlStateManager.rotate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
        } else {
            GlStateManager.rotate(-f3, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
        }
    }

    /**
     * Method to render the coordinate system for the current matrix.
     * Renders three lines with length 1 starting from (0, 0, 0):
     * red line along x axis, green line along y axis and blue line along z axis.
     */
    public void renderCoordinateSystemDebug() {
        if(ConfigurationHandler.getInstance().debug) {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(((float) i) / 16.0F, 0, 0).color(255, 0, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(0, ((float) i) / 16.0F, 0).color(0, 255, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(0, 0, ((float) i) / 16.0F).color(0, 0, 255, 255).endVertex();
            }
            tessellator.draw();

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }

    /**
     * Method to fetch a TextureAtlasSprite icon from a Resource Location
     * @param loc ResourceLocation to grab icon from
     * @return the icon
     */
    public final TextureAtlasSprite getIcon(ResourceLocation loc) {
        return ModelLoader.defaultTextureGetter().apply(loc);
    }
}
