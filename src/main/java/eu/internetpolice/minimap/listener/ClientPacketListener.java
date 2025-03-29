package eu.internetpolice.minimap.listener;

import eu.internetpolice.minimap.XaeroMinimapPaper;
import eu.internetpolice.minimap.entity.MapPlayer;
import eu.internetpolice.minimap.packet.AbstractPacket;
import eu.internetpolice.minimap.packet.HandshakePacket;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class ClientPacketListener implements PluginMessageListener {
    protected final XaeroMinimapPaper plugin;

    public ClientPacketListener(XaeroMinimapPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        // Not our channel, abort:
        if (!channel.equalsIgnoreCase(XaeroMinimapPaper.worldmapChannel) &&
            !channel.equalsIgnoreCase(XaeroMinimapPaper.minimapChannel)) return;

        // Not a registered player, abort:
        if (!plugin.players.containsKey(player.getUniqueId())) return;

        AbstractPacket decodedPacket = decodeClientPacket(message);
        if (decodedPacket instanceof HandshakePacket) {
            MapPlayer mapPlayer = plugin.players.get(player.getUniqueId());
            mapPlayer.receiveHandshake(channel, (HandshakePacket) decodedPacket);
        }
    }

    private AbstractPacket decodeClientPacket(byte[] message) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(message));
        byte packetType = buffer.readByte();

        switch (packetType) {
            case 1 -> { return HandshakePacket.decode(buffer); }
        }

        throw new RuntimeException("Invalid packet type " + packetType);
    }
}
