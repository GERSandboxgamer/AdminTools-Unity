package de.sbg.unity.admintools;

import net.risingworld.api.Server;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Vector3f;

public class atAttribute {

    public final atPlayer Player;
    private final AdminTools plugin;

    public atAttribute(AdminTools plugin) {
        this.Player = new atPlayer();
        this.plugin = plugin;
    }

    public class atPlayer {

        private final String BackPosition;

        public atPlayer() {
            this.BackPosition = "sbg-AdminTool-Attribute-BackPosition";

        }

        public void addAllAttribute(Player player) {
            player.setAttribute(BackPosition, Server.getDefaultSpawnPosition());
            plugin.afk.setAfk(player, false);
        }

        public Vector3f getBackPosition(Player player) {
            return (Vector3f) player.getAttribute(BackPosition);
        }

        public void setBackPosition(Player player, Vector3f pos) {
            player.setAttribute(BackPosition, pos);
        }

    }

}
