package eu.internetpolice.minimap.entity;

import eu.internetpolice.minimap.XaeroMinimapPaper;
import eu.internetpolice.minimap.packet.AbstractPacket;
import eu.internetpolice.minimap.packet.ClientboundPlayerTrackerResetPacket;
import eu.internetpolice.minimap.packet.ClientboundRulesPacket;
import eu.internetpolice.minimap.packet.HandshakePacket;
import eu.internetpolice.minimap.packet.LevelMapPropertiesPacket;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.concurrent.TimeUnit;

/**
 * MapPlayer represents a Bukkit {@link Player}. Before handshake, a MapPlayer could use neither
 * the worldmap nor minimap channel. After handshake, this instance will only persist if using at least one.
 */
public class MapPlayer {
    protected final Player paperPlayer;
    protected final XaeroMinimapPaper plugin;

    protected int networkVersion = -1;
    protected boolean usingWorldmap = false;
    protected boolean usingMinimap = false;

    public MapPlayer(Player paperPlayer, XaeroMinimapPaper plugin) {
        this.paperPlayer = paperPlayer;
        this.plugin = plugin;
    }

    public Player getPaperPlayer() {
        return paperPlayer;
    }

    public int getNetworkVersion() {
        return networkVersion;
    }

    public boolean isUsingWorldmap() {
        return usingWorldmap;
    }

    public boolean isUsingMinimap() {
        return usingMinimap;
    }

    public boolean isActive() {
        return isUsingMinimap() || isUsingWorldmap();
    }

    protected void sendHandshake() {
        if (getNetworkVersion() != -1) {
            throw new RuntimeException("Sending HandshakePacket is only allowed if NetworkVersion is unknown.");
        }

        var handshakePacket = new HandshakePacket();
        sendPacket(handshakePacket);
    }

    public void receiveHandshake(String channel, HandshakePacket packet) {
        this.networkVersion = packet.getNetworkVersion();
        if (channel.equalsIgnoreCase(XaeroMinimapPaper.worldmapChannel)) usingWorldmap = true;
        if (channel.equalsIgnoreCase(XaeroMinimapPaper.minimapChannel)) usingMinimap = true;

        sendLevelProperties();
        sendClientRules();
        sendRadarReset();
    }

    protected void sendLevelProperties() {
        LevelMapPropertiesPacket packet = new LevelMapPropertiesPacket(getPaperPlayer().getWorld());
        sendPacket(packet);
    }

    protected void sendClientRules() {
        CommentedConfigurationNode node = plugin.getConfiguration().node("gamerules");

        ClientboundRulesPacket packet = new ClientboundRulesPacket(
            node.node("allowCaveMode").getBoolean(false),
            node.node("allowNetherCaveMode").getBoolean(false),
            node.node("allowRadar").getBoolean(false)
        );
        sendPacket(packet);
    }

    protected void sendRadarReset() {
        ClientboundPlayerTrackerResetPacket packet = new ClientboundPlayerTrackerResetPacket();
        sendPacket(packet);
    }

    public void sendPacket(AbstractPacket packet) {
        if (getNetworkVersion() == -1 && !(packet instanceof HandshakePacket)) {
            throw new RuntimeException("Handshake isn't done, can only send HandshakePacket");
        }

        byte[] encodedPacket = packet.encode();

        // Sent handshake over both channels, we're unaware of what the player is using:
        if (packet instanceof HandshakePacket) {
            getPaperPlayer().sendPluginMessage(plugin, XaeroMinimapPaper.worldmapChannel, encodedPacket);
            getPaperPlayer().sendPluginMessage(plugin, XaeroMinimapPaper.minimapChannel, encodedPacket);
            return;
        }

        if (isUsingWorldmap()) getPaperPlayer().sendPluginMessage(plugin, XaeroMinimapPaper.worldmapChannel, encodedPacket);
        if (isUsingMinimap()) getPaperPlayer().sendPluginMessage(plugin, XaeroMinimapPaper.minimapChannel, encodedPacket);
    }

    public void connectsToChannel(String channel) {
        sendHandshake();
        plugin.getServer().getAsyncScheduler().runDelayed(plugin,
            (scheduledTask) -> {
                if (getNetworkVersion() == -1) {
                    plugin.players.remove(paperPlayer.getUniqueId());
                }
            },
            10, TimeUnit.SECONDS);
    }

    public void disconnect() {
        plugin.getServer().getAsyncScheduler().runDelayed(plugin,
            (scheduledTask) -> plugin.players.remove(paperPlayer.getUniqueId()),
            1, TimeUnit.SECONDS);
    }
}
