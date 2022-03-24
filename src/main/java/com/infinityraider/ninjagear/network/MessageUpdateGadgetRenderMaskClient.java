package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.ninjagear.render.player.RenderNinjaGadget;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class MessageUpdateGadgetRenderMaskClient extends MessageBase {
    private boolean[] mask;
    private Player player;

    public MessageUpdateGadgetRenderMaskClient() {
        super();
    }

    public MessageUpdateGadgetRenderMaskClient(Player player, boolean[] mask) {
        this();
        this.mask = mask;
        this.player = player;
    }

    @Override
    public NetworkDirection getMessageDirection() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }

    @Override
    protected void processMessage(NetworkEvent.Context ctx) {
        if(this.player != null) {
            RenderNinjaGadget.getInstance().updateRenderMask(this.player, this.mask);
        }
    }
}
