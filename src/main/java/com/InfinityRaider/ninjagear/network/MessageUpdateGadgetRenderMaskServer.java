package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateGadgetRenderMaskServer extends MessageBase<IMessage> {
    private boolean[] mask;

    @SuppressWarnings("unused")
    public MessageUpdateGadgetRenderMaskServer() {}

    public MessageUpdateGadgetRenderMaskServer(boolean[] mask) {
        this.mask = mask;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.getServerHandler().playerEntity != null) {
            new MessageUpdateGadgetRenderMaskClient(ctx.getServerHandler().playerEntity, this.mask).sendToAll();
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
