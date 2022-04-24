package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.utility.registration.ModContentRegistry;
import com.infinityraider.infinitylib.utility.registration.RegistryInitializer;
import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.block.BlockSmoke;

public final class BlockRegistry extends ModContentRegistry {
    private static final BlockRegistry INSTANCE = new BlockRegistry();

    public static BlockRegistry getInstance() {
        return INSTANCE;
    }

    private final RegistryInitializer<BlockSmoke> smoke;
    private final RegistryInitializer<BlockRope> rope;

    private BlockRegistry() {
        this.smoke = this.block(BlockSmoke::new);
        this.rope = this.block(BlockRope::new);
    }

    public final BlockSmoke getSmokeBlock() {
        return this.smoke.get();
    }

    public final BlockRope getRopeBlock() {
        return this.rope.get();
    }
}
