package de.sbg.unity.admintools.Objects;

import de.sbg.unity.admintools.AdminTools;
import de.sbg.unity.admintools.Events.PlayerStartAfkEvent;
import de.sbg.unity.admintools.atConsole;
import net.risingworld.api.Server;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.general.UpdateEvent;
import net.risingworld.api.objects.Player;

public class AutoAfk implements Listener {

    private final Afk Afk;
    private final AdminTools plugin;
    private final atConsole Console;

    public AutoAfk(AdminTools plugin, Afk afk, atConsole Console) {
        this.Afk = afk;
        this.plugin = plugin;
        this.Console = Console;
    }

    @EventMethod
    public void onUpdateEvent(UpdateEvent event) {
        long SystemTime = System.currentTimeMillis();
        long TimeStamp;
        for (Player player : Server.getAllPlayers()) {
            try {
                TimeStamp = Afk.getTimeStamp(player); //Muss kleiner sein als aktuelle Zeit
                if ((TimeStamp < SystemTime) && !Afk.isAfk(player)) {
                    plugin.triggerEvent(new PlayerStartAfkEvent(player, TimeStamp));
                }
            } catch (NullPointerException ex) {
                Console.sendInfo("Wait for TimeStamp at player '" + player.getName() + "'");
            }

        }

    }

}
