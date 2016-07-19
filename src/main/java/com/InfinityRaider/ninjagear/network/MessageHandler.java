package com.infinityraider.ninjagear.network;

import com.infinityraider.ninjagear.NinjaGear;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class MessageHandler<REQ extends MessageBase<REPLY>, REPLY extends IMessage> implements IMessageHandler<REQ, REPLY> {
    protected MessageHandler() {}

    @Override
    public final REPLY onMessage(REQ message, MessageContext ctx) {
        NinjaGear.proxy.queueTask(new Task(message, ctx));
        return message.getReply(ctx);
    }

    private static class Task implements Runnable {
        private final MessageBase message;
        private final MessageContext ctx;

        private Task(MessageBase message, MessageContext ctx) {
            this.message = message;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            this.message.processMessage(this.ctx);
        }
    }
}
