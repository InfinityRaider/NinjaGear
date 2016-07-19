package com.infinityraider.ninjagear.render.entity;

import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.ninjagear.registry.ItemRegistry;
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

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntitySmokeBomb extends Render<EntitySmokeBomb> {
    private final RenderItem renderItem;
    private final ItemStack item;

    private RenderEntitySmokeBomb(RenderManager renderManager) {
        super(renderManager);
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
        this.item = new ItemStack(ItemRegistry.getInstance().itemSmokeBomb);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void doRender(EntitySmokeBomb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
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
    protected ResourceLocation getEntityTexture(EntitySmokeBomb entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public static IRenderFactory<EntitySmokeBomb> getFactory() {
        return Factory.INSTANCE;
    }

    private static class Factory implements IRenderFactory<EntitySmokeBomb> {
        private static Factory INSTANCE = new Factory();

        @Override
        public Render<? super EntitySmokeBomb> createRenderFor(RenderManager manager) {
            return new RenderEntitySmokeBomb(manager);
        }
    }
}
