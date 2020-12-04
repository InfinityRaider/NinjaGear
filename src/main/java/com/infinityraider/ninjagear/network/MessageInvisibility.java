package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.ninjagear.handler.RenderPlayerHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageInvisibility extends MessageBase {
    private PlayerEntity player;
    private boolean invisible;

    public MessageInvisibility() {
        super();
    }

    public MessageInvisibility(PlayerEntity player, boolean invisible) {
        this();
        this.player = player;
        this.invisible = invisible;
    }

    @Override
    public NetworkDirection getMessageDirection() {
        return NetworkDirection.PLAY_TO_SERVER;
    }

    @Override
    protected void processMessage(NetworkEvent.Context ctx) {
        if(this.player != null) {
            RenderPlayerHandler.getInstance().setPlayerInvisibilityStatus(this.player, this.invisible);
        }
    }
}
