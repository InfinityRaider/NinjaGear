package com.infinityraider.ninjagear.render.entity;

import com.infinityraider.infinitylib.render.entity.RenderEntityAsItem;
import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntityRopeCoil extends RenderEntityAsItem<EntityRopeCoil> {
    public RenderEntityRopeCoil(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ItemStack(ItemRegistry.itemRopeCoil));
    }

    @Override
    protected void applyTransformations(EntityRopeCoil entity, float yaw, float partialTicks, PoseStack transforms) {
        //NOOP
    }
}

