package com.infinityraider.ninjagear.effect;

import com.infinityraider.infinitylib.effect.EffectBase;
import com.infinityraider.ninjagear.reference.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class EffectNinjaRevealed extends EffectBase {
    private static final ResourceLocation TEXTURE =  new ResourceLocation(Reference.MOD_ID, "textures/gui/effect_revealed.png");

    public EffectNinjaRevealed()  {
        super("ninja_revealed", EffectType.NEUTRAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public void performEffect(LivingEntity entity, int amplification) {
        entity.setInvisible(false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderHUD(EffectInstance effect) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(EffectInstance effect) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, MatrixStack mStack, int x, int y, float z) {
        mc.getTextureManager().bindTexture(TEXTURE);
        net.minecraft.client.renderer.GlStateManager.color(1, 1, 1, 1);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0F, 0F, 18, 18, 18, 18);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {
        mc.getTextureManager().bindTexture(TEXTURE);
        net.minecraft.client.renderer.GlStateManager.color(1, 1, 1, alpha);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0F, 0F, 18, 18, 18, 18);
    }
}
