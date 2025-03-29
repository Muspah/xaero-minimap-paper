package eu.internetpolice.minimap.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public class HandshakePacket extends AbstractPacket {
    protected final int networkVersion;

    public HandshakePacket() {
        this(2);
    }

    public HandshakePacket(int networkVersion) {
        this.networkVersion = networkVersion;
    }

    @Override
    protected byte getPacketId() {
        return 1;
    }

    public int getNetworkVersion() {
        return networkVersion;
    }

    @Override
    public byte[] encode() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeByte(getPacketId());
        buffer.writeInt(this.networkVersion);
        return encodeBuffer(buffer);
    }

    public static HandshakePacket decode(FriendlyByteBuf buffer) {
        int networkVersion = buffer.readInt();
        return new HandshakePacket(networkVersion);
    }
}
