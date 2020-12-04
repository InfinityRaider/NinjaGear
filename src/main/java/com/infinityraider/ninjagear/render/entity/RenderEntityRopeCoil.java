package com.infinityraider.ninjagear.render.entity;

import com.infinityraider.infinitylib.render.entity.RenderEntityAsItem;
import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntityRopeCoil extends RenderEntityAsItem<EntityRopeCoil> {
    public RenderEntityRopeCoil(EntityRendererManager renderManager) {
        super(renderManager, new ItemStack(ItemRegistry.getInstance().itemRopeCoil));
    }

    @Override
    public void applyTransformations(EntityRopeCoil entity, float yaw, float partialTicks, MatrixStack transforms) {
        //NOOP
    }
}

