package com.infinityraider.ninjagear.effect;

import com.infinityraider.infinitylib.effect.EffectBase;
import com.infinityraider.infinitylib.modules.synchronizedeffects.ISynchronizedEffect;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class EffectNinjaSmoked extends EffectBase implements ISynchronizedEffect {
    public EffectNinjaSmoked()  {
        super(Names.Effects.NINJA_SMOKED, EffectType.NEUTRAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public void performEffect(LivingEntity entity, int amplification) {
        if(entity.isPotionActive(EffectRegistry.getInstance().effectNinjaRevealed)) {
            entity.removeActivePotionEffect(EffectRegistry.getInstance().effectNinjaRevealed);
        }
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
