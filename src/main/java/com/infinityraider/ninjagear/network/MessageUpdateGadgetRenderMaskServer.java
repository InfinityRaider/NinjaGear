package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageUpdateGadgetRenderMaskServer extends MessageBase {
    private boolean[] mask;

    public MessageUpdateGadgetRenderMaskServer() {
        super();
    }

    public MessageUpdateGadgetRenderMaskServer(boolean[] mask) {
        this();
        this.mask = mask;
    }

    @Override
    public NetworkDirection getMessageDirection() {
        return NetworkDirection.PLAY_TO_SERVER;
    }

    @Override
    protected void processMessage(NetworkEvent.Context ctx) {
        if(ctx.getSender() != null) {
            new MessageUpdateGadgetRenderMaskClient(ctx.getSender(), this.mask).sendToAll();
        }
    }
}
