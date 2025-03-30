package eu.internetpolice.minimap;

import eu.internetpolice.minimap.config.Configuration;
import eu.internetpolice.minimap.entity.MapPlayer;
import eu.internetpolice.minimap.event.PlayerEvents;
import eu.internetpolice.minimap.listener.ClientPacketListener;
import eu.internetpolice.minimap.radar.PlayerRadar;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class XaeroMinimapPaper extends JavaPlugin {
    public static final String worldmapChannel = "xaeroworldmap:main";
    public static final String minimapChannel = "xaerominimap:main";

    public ConcurrentMap<UUID, MapPlayer> players = new ConcurrentSkipListMap<>();

    protected Configuration configuration;
    protected PlayerRadar radar;

    @Override
    public void onEnable() {
        this.configuration = new Configuration(this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, worldmapChannel);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, minimapChannel);

        var listener = new ClientPacketListener(this);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, worldmapChannel, listener);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, minimapChannel, listener);

        radar = new PlayerRadar(this);
        this.getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

    public CommentedConfigurationNode getConfiguration() {
        return configuration.getConfig();
    }

    public ConcurrentMap<UUID, MapPlayer> getPlayers() {
        return players;
    }

    public PlayerRadar getRadar() {
        return radar;
    }
}
