package de.sbg.unity.admintools.Events;

import de.sbg.unity.admintools.AdminTools;
import de.sbg.unity.admintools.TextFormat;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.objects.Player;

public class UpdateEvent implements Listener {

    private final AdminTools plugin;
    private final TextFormat textFormat;

    public UpdateEvent(AdminTools plugin) {
        this.plugin = plugin;
        this.textFormat = new TextFormat();
    }

    @EventMethod
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        Player player = event.getPlayer();
        if (player.isAdmin()) {
            player.sendTextMessage(textFormat.Color("orange", "[" + plugin.getDescription("name") + "] Plugin has an update!"));
        }
    }

}
