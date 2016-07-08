package com.infinityraider.ninjagear.network;

import com.infinityraider.ninjagear.render.player.RenderNinjaGadget;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateGadgetRenderMaskClient extends MessageBase<IMessage> {
    private boolean[] mask;
    private EntityPlayer player;

    @SuppressWarnings("unused")
    public MessageUpdateGadgetRenderMaskClient() {}

    public MessageUpdateGadgetRenderMaskClient(EntityPlayer player, boolean[] mask) {
        this.mask = mask;
        this.player = player;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.player != null) {
            RenderNinjaGadget.getInstance().updateRenderMask(this.player, this.mask);
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
        this.player = this.readPlayerFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.mask.length);
        for (boolean aMask : this.mask) {
            buf.writeBoolean(aMask);
        }
        this.writePlayerToByteBuf(buf, this.player);
    }
}
