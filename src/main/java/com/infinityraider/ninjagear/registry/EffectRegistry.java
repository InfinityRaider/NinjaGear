package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.effect.*;
import net.minecraft.potion.Effect;

public class EffectRegistry {
    private static final EffectRegistry INSTANCE = new EffectRegistry();

    public static EffectRegistry getInstance() {
        return INSTANCE;
    }

    public Effect effectNinjaHidden = new EffectNinjaHidden();
    public Effect effectNinjaRevealed = new EffectNinjaRevealed();

    private EffectRegistry() {}
}
