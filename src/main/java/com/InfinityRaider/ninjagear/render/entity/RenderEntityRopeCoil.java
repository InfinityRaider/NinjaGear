package com.InfinityRaider.ninjagear.render.entity;

import com.InfinityRaider.ninjagear.entity.EntityRopeCoil;
import com.InfinityRaider.ninjagear.registry.ItemRegistry;
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

@SideOnly(Side.CLIENT)
public class RenderEntityRopeCoil  extends Render<EntityRopeCoil> {
    private final RenderItem renderItem;
    private final ItemStack item;

    private RenderEntityRopeCoil(RenderManager renderManager) {
        super(renderManager);
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
        this.item = new ItemStack(ItemRegistry.getInstance().itemRopeCoil);
    }

    @Override
    public void doRender(EntityRopeCoil entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(TextureMap.locationBlocksTexture);
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
    protected ResourceLocation getEntityTexture(EntityRopeCoil entity) {
        return TextureMap.locationBlocksTexture;
    }

    public static IRenderFactory<EntityRopeCoil> getFactory() {
        return Factory.INSTANCE;
    }

    private static class Factory implements IRenderFactory<EntityRopeCoil> {
        private static Factory INSTANCE = new Factory();

        @Override
        public Render<? super EntityRopeCoil> createRenderFor(RenderManager manager) {
            return new RenderEntityRopeCoil(manager);
        }
    }
}
