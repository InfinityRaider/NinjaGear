package com.InfinityRaider.ninjagear.render.entity;

import com.InfinityRaider.ninjagear.entity.EntityShuriken;
import com.InfinityRaider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntityShuriken extends Render<EntityShuriken> {
    private final RenderItem renderItem;
    private final ItemStack item;

    private RenderEntityShuriken(RenderManager renderManager) {
        super(renderManager);
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
        this.item = new ItemStack(ItemRegistry.getInstance().itemShuriken);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void doRender(EntityShuriken entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        //translate and rotate according to throwing heading
        double vX = entity.getDirection().xCoord;
        double vY = entity.getDirection().yCoord;
        double vZ = entity.getDirection().zCoord;
        double alpha = 180*Math.atan2(vZ, vX)/Math.PI;
        double beta = 180*Math.atan2(vY, Math.sqrt(vX*vX + vZ*vZ))/Math.PI;
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate((float) -alpha, 0, 1, 0);
        GlStateManager.rotate((float) beta, 0, 0, 1);

        //rotate around z-axis
        int period = 600;
        int time = (int) (System.currentTimeMillis() % period);
        float angle = -360 * ((float) time) / ((float) period);
        float dy = -0.125F;
        GL11.glRotatef(angle, 0, 0, 1);
        GL11.glTranslatef(0, dy, 0);

        GlStateManager.enableRescaleNormal();

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.renderItem.renderItem(this.item, ItemCameraTransforms.TransformType.GROUND);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected ResourceLocation getEntityTexture(EntityShuriken entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public static IRenderFactory<EntityShuriken> getFactory() {
        return Factory.INSTANCE;
    }

    private static class Factory implements IRenderFactory<EntityShuriken> {
        private static Factory INSTANCE = new Factory();

        @Override
        public Render<? super EntityShuriken> createRenderFor(RenderManager manager) {
            return new RenderEntityShuriken(manager);
        }
    }
}
