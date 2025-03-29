package eu.internetpolice.minimap.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public class LevelMapPropertiesPacket extends AbstractPacket {
    private final int id = 123456789;

    @Override
    protected byte getPacketId() {
        return 0;
    }

    @Override
    public byte[] encode() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeByte(getPacketId());
        buffer.writeInt(id);
        return encodeBuffer(buffer);
    }
}
