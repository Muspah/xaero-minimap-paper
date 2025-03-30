package eu.internetpolice.minimap.packet;

public class ClientboundPlayerTrackerResetPacket extends AbstractPacket {
    @Override
    protected byte getPacketId() {
        return 3;
    }

    @Override
    public byte[] encode() {
        return encodeBuffer(getPacket());
    }
}
