package com.infinityraider.ninjagear.potion;

import com.infinityraider.infinitylib.modules.synchronizedeffects.ISynchronizedEffect;
import com.infinityraider.infinitylib.potion.PotionEffectBase;
import com.infinityraider.ninjagear.reference.Names;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class PotionEffectNinjaRevealed extends PotionEffectBase implements ISynchronizedEffect {
    public PotionEffectNinjaRevealed()  {
        super(Names.Effects.NINJA_REVEALED, MobEffectCategory.NEUTRAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplification) {
        entity.setInvisible(false);
    }


    //TODO
    /*
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
    */
}
