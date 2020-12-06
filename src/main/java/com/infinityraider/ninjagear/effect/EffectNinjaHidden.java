package com.infinityraider.ninjagear.effect;

import com.infinityraider.infinitylib.effect.EffectBase;
import com.infinityraider.infinitylib.modules.synchronizedeffects.ISynchronizedEffect;
import com.infinityraider.ninjagear.reference.Names;
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

public class EffectNinjaHidden extends EffectBase implements ISynchronizedEffect {
    private static final ResourceLocation TEXTURE =  new ResourceLocation(Reference.MOD_ID, "textures/gui/effect_hidden.png");

    public EffectNinjaHidden() {
        super(Names.Effects.NINJA_HIDDEN, EffectType.BENEFICIAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplification) {
        entity.setInvisible(true);
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
        this.drawInGui(mStack, x, y, gui, 18, 18, 1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {
        this.drawInGui(mStack, x, y, gui, 18, 18, alpha);
    }
}
