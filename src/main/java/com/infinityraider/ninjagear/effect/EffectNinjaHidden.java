package com.infinityraider.ninjagear.effect;

import com.infinityraider.infinitylib.effect.EffectBase;
import com.infinityraider.infinitylib.modules.synchronizedeffects.ISynchronizedEffect;
import com.infinityraider.ninjagear.reference.Names;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class EffectNinjaHidden extends EffectBase implements ISynchronizedEffect {
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
}
