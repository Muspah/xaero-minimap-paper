package eu.internetpolice.minimap.radar;

import eu.internetpolice.minimap.XaeroMinimapPaper;
import eu.internetpolice.minimap.packet.ClientboundTrackedPlayerPacket;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerRadar {
    protected final XaeroMinimapPaper plugin;

    public PlayerRadar(XaeroMinimapPaper plugin) {
        this.plugin = plugin;
    }

    public void playerReset(Player player) {
        ClientboundTrackedPlayerPacket positionPacket = new ClientboundTrackedPlayerPacket(true, player, player.getLocation());
        sendToApplicablePlayers(player, positionPacket);
    }

    public void positionUpdate(Player player, Location position) {
        ClientboundTrackedPlayerPacket positionPacket = new ClientboundTrackedPlayerPacket(false, player, position);
        sendToApplicablePlayers(player, positionPacket);
    }

    protected void sendToApplicablePlayers(Player player, ClientboundTrackedPlayerPacket packet) {
        plugin.getPlayers().values().forEach(mapPlayer -> {
            if (!mapPlayer.isActive()) return;
            if (mapPlayer.getPaperPlayer().equals(player)) return;

            if (!packet.isRemove() && mapPlayer.getPaperPlayer().getLocation().getWorld().equals(player.getLocation().getWorld())) {
                mapPlayer.sendPacket(packet);
            } else if (packet.isRemove()) {
                mapPlayer.sendPacket(packet);
            }
        });
    }
}
