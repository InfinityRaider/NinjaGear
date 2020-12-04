package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.block.BlockSmoke;
import net.minecraft.block.Block;

public class BlockRegistry {
    private static final BlockRegistry INSTANCE = new BlockRegistry();

    public static BlockRegistry getInstance() {
        return INSTANCE;
    }

    private BlockRegistry() {
        this.blockSmoke = new BlockSmoke();
        this.blockRope = new BlockRope();
    }

    public Block blockSmoke;
    public Block blockRope;
}
