package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.ninjagear.handler.RenderPlayerHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class MessageInvisibility extends MessageBase {
    private Player player;
    private boolean invisible;

    public MessageInvisibility() {
        super();
    }

    public MessageInvisibility(Player player, boolean invisible) {
        this();
        this.player = player;
        this.invisible = invisible;
    }

    @Override
    public NetworkDirection getMessageDirection() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }

    @Override
    protected void processMessage(NetworkEvent.Context ctx) {
        if(this.player != null) {
            RenderPlayerHandler.getInstance().setPlayerInvisibilityStatus(this.player, this.invisible);
        }
    }
}
