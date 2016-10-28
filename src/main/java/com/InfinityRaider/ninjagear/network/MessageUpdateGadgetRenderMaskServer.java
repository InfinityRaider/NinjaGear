package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.ninjagear.NinjaGear;
import io.netty.buffer.ByteBuf;
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
        if(ctx.side == Side.SERVER && ctx.getServerHandler().playerEntity != null) {
            NinjaGear.instance.getNetworkWrapper().sendToAll(new MessageUpdateGadgetRenderMaskClient(ctx.getServerHandler().playerEntity, this.mask));
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.mask = new boolean[buf.readInt()];
        for(int i = 0; i < this.mask.length; i++) {
            this.mask[i] = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.mask.length);
        for (boolean aMask : this.mask) {
            buf.writeBoolean(aMask);
        }
    }
}
