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

public class EffectNinjaRevealed extends EffectBase implements ISynchronizedEffect {
    public EffectNinjaRevealed()  {
        super(Names.Effects.NINJA_REVEALED, EffectType.NEUTRAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public void performEffect(LivingEntity entity, int amplification) {
        entity.setInvisible(false);
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
