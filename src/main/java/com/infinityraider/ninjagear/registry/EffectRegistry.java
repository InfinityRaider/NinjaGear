package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.effect.EffectNinjaAura;
import com.infinityraider.ninjagear.effect.EffectNinjaHidden;
import com.infinityraider.ninjagear.effect.EffectNinjaRevealed;
import net.minecraft.potion.Effect;

public class EffectRegistry {
    private static final EffectRegistry INSTANCE = new EffectRegistry();

    public static EffectRegistry getInstance() {
        return INSTANCE;
    }

    public Effect potionNinjaAura = new EffectNinjaAura();
    public Effect potionNinjaHidden = new EffectNinjaHidden();
    public Effect potionNinjaRevealed = new EffectNinjaRevealed();

    private EffectRegistry() {}
}
