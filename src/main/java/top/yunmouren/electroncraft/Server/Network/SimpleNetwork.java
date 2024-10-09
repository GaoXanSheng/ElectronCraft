package top.yunmouren.electroncraft.Server.Network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import top.yunmouren.electroncraft.Client.Network.ClientEnumeration;

import java.util.function.Supplier;

public class SimpleNetwork {
    private final String Head;
    private final String Body;

    public SimpleNetwork(String Head, String Body) {
        this.Head = Head;
        this.Body = Body;
    }
    public SimpleNetwork(String Head) {
        this.Head = Head;
        this.Body = null;
    }
    public static void encode(SimpleNetwork message, FriendlyByteBuf buffer) {
        buffer.writeUtf(message.Head + "㽬" + message.Body);
    }

    public static SimpleNetwork decode(FriendlyByteBuf buffer) {
        String[] decode = buffer.readUtf().split("㽬");
        return new SimpleNetwork(decode[0], decode[1]);
    }

    public static void handle(SimpleNetwork message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            new ClientEnumeration(message.Head,message.Body);
        });
        context.setPacketHandled(true);
    }
}
