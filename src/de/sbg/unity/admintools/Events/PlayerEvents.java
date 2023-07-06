package de.sbg.unity.admintools.Events;

import de.sbg.unity.admintools.AdminTools;
import de.sbg.unity.admintools.TextFormat;
import de.sbg.unity.admintools.atConsole;
import net.risingworld.api.Server;
import net.risingworld.api.definitions.Definitions;
import net.risingworld.api.definitions.WeatherDefs;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerDeathEvent;
import net.risingworld.api.events.player.PlayerTeleportEvent;
import net.risingworld.api.objects.Player;

public class PlayerEvents implements Listener {

    private final AdminTools plugin;
    private final atConsole Console;
    private final TextFormat TextFormat;
    private final EventElements Elements;

    public PlayerEvents(AdminTools plugin, atConsole Console) {
        this.plugin = plugin;
        this.Console = Console;
        this.TextFormat = new TextFormat();
        this.Elements = new EventElements();
    }

    @EventMethod
    public void onPlayerCommandEvent(PlayerCommandEvent event) {
        Player player = event.getPlayer();
        Player player2 = null;
        String[] cmd = event.getCommand().split(" ");
        String[] cmd2 = event.getCommand().split(" ", 2);
        String[] cmd3 = event.getCommand().split(" ", 3);

        if (cmd.length >= 2) {
            if (cmd[0].toLowerCase().equals("/kickall")) {
                if (player.isAdmin()) {
                    for (Player p2 : Server.getAllPlayers()) {
                        p2.kick(cmd2[1]);
                    }
                }
            }

            if (cmd[0].toLowerCase().equals("/sudo")) {
                if (player.isAdmin()) {
                    player.executeCommand(cmd2[1]);
                    player.sendTextMessage(TextFormat.Color("green", "Execute command!"));
                }
            }

        }

        if (cmd.length >= 3) {
            if (cmd[0].toLowerCase().equals("/broadcast") || cmd[0].toLowerCase().equals("/bc")) {
                if (player.isAdmin()) {
                    if (cmd[1].toLowerCase().equals("text")) {
                        Server.broadcastTextMessage(cmd3[2]);
                    }
                    if (cmd[1].toLowerCase().equals("yell")) {
                        Server.broadcastYellMessage(cmd3[2], 5f, true);
                    }
                }
            }

            if (cmd[0].toLowerCase().equals("/kick")) {
                if (player.isAdmin()) {
                    player2 = Server.getPlayerByName(cmd3[1]);
                    if (player2 != null && player2.isConnected()) {
                        player2.kick(cmd3[2]);
                    }
                }
            }
        }

        if (cmd.length == 1) {

            if (cmd[0].toLowerCase().equals("/help")) {
                Help(player, 1, null);
            }
            if (cmd[0].toLowerCase().equals("/afk")) {
                if (!plugin.config.Command_Afk || player.isAdmin()) {
                    if (plugin.afk.isAfk(player)) {
                        plugin.afk.setAfk(player, false);
                        player.sendTextMessage(TextFormat.Color("green", "You are not longer AFK!"));
                        for (Player p : Server.getAllPlayers()) {
                            p.sendTextMessage(TextFormat.Color("orange", "Player '" + player.getName() + "' is not longer AFK!"));
                        }
                    } else {
                        plugin.afk.setAfk(player, true);
                        player.sendTextMessage(TextFormat.Color("green", "You are now AFK!"));
                        for (Player p : Server.getAllPlayers()) {
                            p.sendTextMessage(TextFormat.Color("orange", "Player '" + player.getName() + "' is now AFK!"));
                        }
                    }

                }
            }

            if (cmd[0].toLowerCase().equals("/vanish")) {
                if (player.isAdmin()) {
                    if (player.isInVehicle()) {
                        player.setInvisible(false);
                        player.sendTextMessage(TextFormat.Color("green", "You are not longer invisible!"));
                    } else {
                        player.setInvisible(true);
                        player.sendTextMessage(TextFormat.Color("green", "You are now invisible!"));
                    }

                }
            }

            if (cmd[0].toLowerCase().equals("/fly")) {
                if (player.isAdmin()) {
                    if (player.isFlying()) {
                        player.setFlying(false);
                        player.sendTextMessage(TextFormat.Color("green", "Flying desabled!"));
                    } else {
                        player.setFlying(true);
                        player.sendTextMessage(TextFormat.Color("green", "Flying enabled!"));
                    }
                }
            }

            if (cmd[0].toLowerCase().equals("/near")) {
                if (player.isAdmin()) {
                    player2 = getNearestPlayer(player);
                    if (player2 != null) {
                        player.sendTextMessage(TextFormat.Color("green", "Nearest player: " + TextFormat.Bold(player2.getName())));
                    } else {
                        player.sendTextMessage(TextFormat.Color("red", "No player in the near!"));
                    }
                }
            }

            if (cmd[0].toLowerCase().equals("/compass") || cmd[0].toLowerCase().equals("/com")) {
                if (!plugin.config.Command_Compass || player.isAdmin()) {
                    player.sendTextMessage(TextFormat.Color("orange", "Compass: " + TextFormat.Bold(player.getCardinalDirection())));
                }
            }

//            if (cmd[0].toLowerCase().equals("/more")) {
//                if (player.isAdmin()) {
//                    Item item = player.getEquippedItem();
//                    int Rest = item.getMaxStackSize() - item.getStack();
//                    //TODO Command: more
//                }
//            }
            if (cmd[0].toLowerCase().equals("/clear") || cmd[0].toLowerCase().equals("/clearinventory")) {
                if (!plugin.config.Command_Clear || player.isAdmin()) {
                    player.getInventory().clear();
                    player.sendTextMessage(TextFormat.Color("green", "Inventory successfully cleared!"));
                }
            }

            if (cmd[0].toLowerCase().equals("/back")) {
                if (!plugin.config.Command_Back || player.isAdmin()) {
                    if (plugin.attribute.Player.getBackPosition(player) != null) {
                        player.setPosition(plugin.attribute.Player.getBackPosition(player));
                        plugin.attribute.Player.setBackPosition(player, null);
                        player.sendTextMessage(TextFormat.Color("green", "You are Back!"));
                    }
                }
            }

            if (cmd[0].toLowerCase().equals("/players")) {
                if (!plugin.config.Command_Players || player.isAdmin()) {
                    player.sendTextMessage(TextFormat.Color("orange", "-------- Player-List --------"));
                    for (Player p2 : Server.getAllPlayers()) {
                        if (p2.isAdmin()) {
                            player.sendTextMessage(TextFormat.Color("orange", "- " + p2.getName() + " (Group: " + p2.getPermissionGroup() + ") ") + TextFormat.Color("red", TextFormat.Bold("[Admin]")));
                        } else {
                            player.sendTextMessage(TextFormat.Color("orange", "- " + p2.getName() + " (Group: " + p2.getPermissionGroup() + ")"));
                        }
                    }
                    player.sendTextMessage(TextFormat.Color("orange", "-----------------------------"));

                }
            }

            if (cmd[0].toLowerCase().equals("/heal")) {
                if (player.isAdmin()) {
                    player.setHealth(player.getMaxHealth());
                    player.setStamina(player.getMaxStamina());
                    player.setBrokenBones(false);
                    player.setBleeding(false);
                    player.setHunger(100);
                    player.setThirst(100);
                    player.sendTextMessage(TextFormat.Color("green", "You have been completely healed!"));
                }
            }
            if (cmd[0].toLowerCase().equals("/spawn")) {
                if (!plugin.config.Command_Spawn || player.isAdmin()) {
                    plugin.attribute.Player.setBackPosition(player, player.getPosition());
                    player.setPosition(Server.getDefaultSpawnPosition());
                    player.sendTextMessage(TextFormat.Color("green", "Teleport you to the server spawnpoint!"));
                }
            }

        }

        if (cmd.length >= 2) {
            if (cmd.length == 2) {

                if (cmd[0].toLowerCase().equals("/fly")) {
                    if (player.isAdmin()) {
                        switch (cmd[1]) {
                            case "on" -> {
                                player.setFlying(true);
                                player.sendTextMessage(TextFormat.Color("green", "Flying enabled!"));
                            }
                            case "off" -> {
                                player.setFlying(false);
                                player.sendTextMessage(TextFormat.Color("green", "Flying desabled!"));
                            }
                            default -> {
                                player2 = Server.getPlayerByName(cmd[1]);
                                if (player2 != null && player2.isConnected()) {
                                    if (player2.isFlying()) {
                                        player2.setFlying(false);
                                        player.sendTextMessage(TextFormat.Color("green", "Flying desabled for '" + cmd[1] + "'!"));
                                        player2.sendTextMessage(TextFormat.Color("red", "Flying desabled!"));
                                    } else {
                                        player2.setFlying(true);
                                        player.sendTextMessage(TextFormat.Color("green", "Flying enabled for '" + cmd[1] + "'!"));
                                        player2.sendTextMessage(TextFormat.Color("green", "Flying enabled!"));
                                    }
                                } else {
                                    player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                                }
                            }

                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/help")) {
                    int i = Elements.toNumber(cmd[1]);
                    if (i >= 1) {
                        Help(player, i);
                    } else {
                        Help(player, cmd[1]);
                    }
                }

                if (cmd[0].toLowerCase().equals("/clear") || cmd[0].toLowerCase().equals("/clearinventory")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {
                            player.getInventory().clear();
                            player.sendTextMessage(TextFormat.Color("green", "Inventory of '" + cmd[1] + "' successfully cleared!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }
                }

//                if (cmd[0].toLowerCase().equals("spawnnpc") || cmd[0].toLowerCase().equals("npc")) {
//                    if (player.isAdmin()) {
//                        //TODO Command: spawnnpc|npc
//                    }
//                }
                if (cmd[0].toLowerCase().equals("/spawn")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {
                            plugin.attribute.Player.setBackPosition(player2, player2.getPosition());
                            player2.setPosition(Server.getDefaultSpawnPosition());
                            player.sendTextMessage(TextFormat.Color("green", "Teleport player '" + cmd[1] + "' to the server spawnpoint!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/weather")) {
                    if (player.isAdmin()) {
                        if (cmd[1].toLowerCase().equals("list")) {
                            player.sendTextMessage(TextFormat.Color("orange", "------- Weather-List -------"));
                            for (String s : Elements.getWeatherList()) {
                                player.sendTextMessage(TextFormat.Color("orange", "- " + s));
                            }
                            player.sendTextMessage(TextFormat.Color("orange", "- true -> Turn the weather on"));
                            player.sendTextMessage(TextFormat.Color("orange", "- false -> Turn the weather off"));
                            player.sendTextMessage(TextFormat.Color("orange", "----------------------------"));
                        } else if (cmd[1].toLowerCase().equals("true") || cmd[1].toLowerCase().equals("false")) {
                            Server.setWeatherEnabled(Boolean.parseBoolean(cmd[1]));
                            player.sendTextMessage(TextFormat.Color("green", "Weather enabled = " + cmd[1]));
                        } else {
                            if (Elements.getWeatherList().contains(cmd[1])) {
                                WeatherDefs.Weather w = Definitions.getWeather(cmd[1]);
                                Server.setWeather(w, false);
                                player.sendTextMessage(TextFormat.Color("green", "You set the weather to '" + cmd[1] + "'!"));

                            } else {
                                player.sendTextMessage(TextFormat.Color("red", "'" + cmd[1] + "' is not a weather typ!"));
                            }
                        }
                    }

                }

                if (cmd[0].toLowerCase().equals("/playerinfo")) {
                    if (player.isAdmin()) {
                        if (!cmd[1].toLowerCase().equals("#nearest")) {
                            player2 = Server.getPlayerByName(cmd[1]);
                        } else {
                            player2 = Server.findNearestPlayer(player.getPosition());
                        }
                        if (player2 != null && player2.isConnected()) {
                            String pos = String.valueOf(player2.getPosition().x + ", " + player2.getPosition().y + ", " + player2.getPosition().z);

                            player.sendTextMessage(TextFormat.Color("orange", "-------- Player-Info --------"));
                            player.sendTextMessage(TextFormat.Color("orange", "    Name: " + cmd[1]));
                            player.sendTextMessage(TextFormat.Color("orange", "     UID: " + player2.getUID()));
                            player.sendTextMessage(TextFormat.Color("orange", " Game-ID: " + player2.getIP()));
                            player.sendTextMessage(TextFormat.Color("orange", "   DB-ID: " + player2.getDbID()));
                            player.sendTextMessage(TextFormat.Color("orange", "Platform: " + player2.getPlatform()));
                            player.sendTextMessage(TextFormat.Color("orange", "   Admin: " + (player2.isAdmin() ? "Yes" : "No")));
                            player.sendTextMessage(TextFormat.Color("orange", "Language: " + player2.getLanguage()));
                            player.sendTextMessage(TextFormat.Color("orange", "   Group: " + player2.getPermissionGroup()));
                            player.sendTextMessage(TextFormat.Color("orange", "Position: " + pos + " (X, Y, Z)"));
                            player.sendTextMessage(TextFormat.Color("orange", "Creative: " + (player2.isCreativeModeEnabled() ? "Yes" : "No")));
                            player.sendTextMessage(TextFormat.Color("orange", "-----------------------------"));

                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }

                    }

                }

                if (cmd[0].toLowerCase().equals("/gm")) {
                    if (player.isAdmin()) {
                        if (cmd[1].toLowerCase().equals("c") || cmd[1].toLowerCase().equals("creative") || cmd[1].equals("1")) {
                            player.setCreativeModeEnabled(true);
                            player.sendTextMessage(TextFormat.Color("green", "Change Gamemode to creative!"));
                        }
                        if (cmd[1].toLowerCase().equals("s") || cmd[1].toLowerCase().equals("survivel") || cmd[1].equals("0")) {
                            player.setCreativeModeEnabled(false);
                            player.sendTextMessage(TextFormat.Color("green", "Change Gamemode to survivel!"));
                        }
                    }
                }
                if (cmd[0].toLowerCase().equals("/tpto")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {
                            player.setPosition(player.getPosition());
                            player.sendTextMessage(TextFormat.Color("green", "Teleport you to '" + cmd[1] + "'!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }
                }
                if (cmd[0].toLowerCase().equals("/tphere")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {
                            player2.setPosition(player.getPosition());
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/heal")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {
                            player2.setHealth(player.getMaxHealth());
                            player2.setStamina(player.getMaxStamina());
                            player2.setBrokenBones(false);
                            player2.setBleeding(false);
                            player2.setHunger(100);
                            player2.setThirst(100);
                            player.sendTextMessage(TextFormat.Color("green", "The player '" + cmd[1] + " have been completely healed!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/time")) {
                    if (player.isAdmin()) {
                        if (Elements.getTimeList().contains(cmd[1])) {
                            switch (cmd[1]) {
                                case "day" ->
                                    Server.setGameTime(9, 0);
                                case "night" ->
                                    Server.setGameTime(21, 0);
                                case "middnight" ->
                                    Server.setGameTime(0, 0);
                                case "evening" ->
                                    Server.setGameTime(18, 0);
                                case "noon" ->
                                    Server.setGameTime(12, 0);
                                case "afternoon" ->
                                    Server.setGameTime(15, 0);
                            }
                            player.sendTextMessage(TextFormat.Color("green", "The ingame time has been successfully changed!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "The weather type '" + cmd[1] + "' doesn't exist!"));
                        }
                    }
                }

            }
            if (cmd.length == 3) {

                if (cmd[0].toLowerCase().equals("/fly")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {
                            switch (cmd[2].toLowerCase()) {
                                case "off" -> {
                                    player.setFlying(false);
                                    player.sendTextMessage(TextFormat.Color("green", "Flying desabled for '" + cmd[1] + "'!"));
                                    player2.sendTextMessage(TextFormat.Color("red", "Flying desabled!"));
                                }
                                case "on" -> {
                                    player.setFlying(true);
                                    player.sendTextMessage(TextFormat.Color("green", "Flying enabled for '" + cmd[1] + "'!"));
                                    player2.sendTextMessage(TextFormat.Color("green", "Flying enabled!"));
                                }
                                default -> {
                                    player.sendTextMessage(TextFormat.Color("red", "Use: on / off"));
                                }
                            }
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }

                }

                if (cmd[0].toLowerCase().equals("/tp")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        Player player3 = Server.getPlayerByName(cmd[2]);
                        if ((player2 != null && player2.isConnected()) && (player3 != null && player3.isConnected())) {
                            player2.setPosition(player3.getPosition());
                            plugin.attribute.Player.setBackPosition(player2, player2.getPosition());
                            player.sendTextMessage(TextFormat.Color("green", "Teleport player '" + cmd[1] + "' to player '" + cmd[2] + "'!"));
                        } else {
                            if (player2 == null) {
                                player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                            }
                            if (player3 == null) {
                                player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[2] + "' not connected!"));
                            }
                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/time")) {
                    if (player.isAdmin()) {
                        int h = 0, m = 0;
                        boolean form = false;
                        try {
                            h = Integer.parseInt(cmd[1]);
                            m = Integer.parseInt(cmd[2]);
                            form = true;
                        } catch (NumberFormatException ex) {
                        }
                        if (form) {
                            Server.setGameTime(h, m);
                            player.sendTextMessage(TextFormat.Color("green", "The ingame time has been successfully changed!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "The ingame time could not be changed!"));
                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/gm")) {
                    if (player.isAdmin()) {
                        if (cmd[1].toLowerCase().equals("c") || cmd[1].toLowerCase().equals("creative") || cmd[1].equals("1")) {
                            player2 = Server.getPlayerByName(cmd[2]);
                            if (player2 != null && player2.isConnected()) {
                                player2.setCreativeModeEnabled(true);
                                player.sendTextMessage(TextFormat.Color("green", "Change the Gamemode from player '" + cmd[2] + "' to creative!"));
                            } else {
                                player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[2] + "' not connected!"));
                            }
                        }
                        if (cmd[1].toLowerCase().equals("s") || cmd[1].toLowerCase().equals("survivel") || cmd[1].equals("0")) {
                            player2 = Server.getPlayerByName(cmd[2]);
                            if (player2 != null && player2.isConnected()) {
                                player2.setCreativeModeEnabled(false);
                                player.sendTextMessage(TextFormat.Color("green", "Change the Gamemode from player '" + cmd[2] + "' to survivel!"));
                            } else {
                                player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[2] + "' not connected!"));
                            }
                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/heal")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {

                            switch (cmd[2]) {
                                case "all" -> {
                                    player2.setHealth(player.getMaxHealth());
                                    player2.setStamina(player.getMaxStamina());
                                    player2.setBrokenBones(false);
                                    player2.setBleeding(false);
                                    player2.setHunger(100);
                                    player2.setThirst(100);
                                }
                                case "thirst" ->
                                    player2.setThirst(100);
                                case "hunger" ->
                                    player2.setHunger(100);
                                case "bleeding" ->
                                    player2.setBleeding(false);
                                case "brokenbones" ->
                                    player2.setBrokenBones(false);
                                case "stamina" ->
                                    player2.setStamina(player.getMaxStamina());
                                case "health" ->
                                    player2.setHealth(player.getMaxHealth());

                            }
                            player.sendTextMessage(TextFormat.Color("green", "Heal " + cmd[2] + " by player '" + cmd[1] + "'!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }
                }

                if (cmd[0].toLowerCase().equals("/weather")) {
                    if (player.isAdmin()) {
                        if (Elements.getWeatherList().contains(cmd[1])) {
                            WeatherDefs.Weather w = Definitions.getWeather(cmd[1]);
                            Server.setWeather(w, Boolean.parseBoolean(cmd[2]));
                            player.sendTextMessage(TextFormat.Color("green", "Weather successfully changed to '" + cmd[1] + "'!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Weather could not be changed to '" + cmd[1] + "'!"));
                        }
                    }
                }
            }

            if (cmd.length == 4) {

                if (cmd[0].toLowerCase().equals("/tp")) {
                    if (player.isAdmin()) {
                        float f1, f2, f3;
                        f1 = Elements.toFlaot(cmd[1]);
                        f2 = Elements.toFlaot(cmd[2]);
                        f3 = Elements.toFlaot(cmd[3]);
                        if (f1 >= 0 && f2 >= 0 && f3 >= 0) {
                            player.setPosition(f1, f2, f3);
                            plugin.attribute.Player.setBackPosition(player, player.getPosition());
                            player.sendTextMessage(TextFormat.Color("green", "Teleport you!"));
                        }
                    }
                }
            }
            if (cmd.length == 5) {

                if (cmd[0].toLowerCase().equals("/tp")) {
                    if (player.isAdmin()) {
                        player2 = Server.getPlayerByName(cmd[1]);
                        if (player2 != null && player2.isConnected()) {
                            float f1, f2, f3;
                            f1 = Elements.toFlaot(cmd[2]);
                            f2 = Elements.toFlaot(cmd[3]);
                            f3 = Elements.toFlaot(cmd[4]);
                            if (f1 >= 0 && f2 >= 0 && f3 >= 0) {
                                player.setPosition(f1, f2, f3);
                                plugin.attribute.Player.setBackPosition(player, player.getPosition());
                                player.sendTextMessage(TextFormat.Color("green", "Teleport player '" + cmd[1] + "'!"));
                            }
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Player '" + cmd[1] + "' not connected!"));
                        }
                    }
                }
            }
        }
    }

    @EventMethod
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        Player player = event.getPlayer();
        plugin.attribute.Player.addAllAttribute(player);
        if (plugin.config.WelcomeMessage_Enabled) {
            String msg;
            msg = plugin.config.WelcomeMessage.replace("%SERVER%", Server.getName()).replace("%PLAYER%", player.getName());
            player.sendTextMessage(msg);
        }
    }

    @EventMethod
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        plugin.attribute.Player.setBackPosition(player, event.getDeathPosition());
    }

    @EventMethod
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        plugin.attribute.Player.setBackPosition(player, player.getPosition());

    }

    private void Help(Player player, String f) {
        Help(player, -1, f);
    }

    private void Help(Player player, int page) {
        Help(player, page, null);
    }

    private void Help(Player player, int page, String f) {
        if (page > 3) {
            page = 3;
        }
        if (!player.isAdmin()) {
            player.sendTextMessage(TextFormat.Color("orange", "-------- Help --------"));
            player.sendTextMessage(TextFormat.Color("orange", "/afk - Make you afk"));
            if (!plugin.config.Command_Back) {
                player.sendTextMessage(TextFormat.Color("orange", "/back - teleport you back to your last position"));
                player.sendTextMessage(TextFormat.Color("orange", "-> Teleport-Position, Death-Position"));
            }
            if (!plugin.config.Command_Clear) {
                player.sendTextMessage(TextFormat.Color("orange", "/clear - Clear your inventory"));
            }
            if (!plugin.config.Command_Compass) {
                player.sendTextMessage(TextFormat.Color("orange", "/[compass|com] - Show the cardinal direction"));
            }
            player.sendTextMessage(TextFormat.Color("orange", "/help - Show the help"));
            if (!plugin.config.Command_Players) {
                player.sendTextMessage(TextFormat.Color("orange", "/players - Show a list of all players with they groups"));
            }
            if (!plugin.config.Command_Spawn) {
                player.sendTextMessage(TextFormat.Color("orange", "/spawn - Teleport you to the server spawnpoint"));
            }
        } else {

            if (page != -1) {
                switch (page) {
                    case 1 -> {
                        player.sendTextMessage(TextFormat.Color("orange", "-------- Help (1/3) --------"));
                        player.sendTextMessage(TextFormat.Color("yellow", "<> = Mandatory information; () = Optinal; | = Or"));
                        player.sendTextMessage(TextFormat.Color("orange", "/afk - Make you afk"));
                        player.sendTextMessage(TextFormat.Color("orange", "/back - Send you back to the dirth- or teleport-position"));
                        player.sendTextMessage(TextFormat.Color("orange", "/<broadcast|bc> <text|yell> <msg> - Send a broadcast message"));
                        player.sendTextMessage(TextFormat.Color("orange", "/clear - Clear your inventory"));
                        player.sendTextMessage(TextFormat.Color("orange", "/<compass|com> - Show the cardinal direction"));
                        player.sendTextMessage(TextFormat.Color("orange", "/fly (on|off)"));
                        player.sendTextMessage(TextFormat.Color("orange", "/fly <Playername> (on|off)"));
                        player.sendTextMessage(TextFormat.Color("orange", "/gm <Typ> (Playername) - Set Gamemode"));
                        player.sendTextMessage(TextFormat.Color("orange", " -> Typ: Creativ: c, creative, 1; Survivel: s, survivel, 0"));
                        player.sendTextMessage(TextFormat.Color("orange", "/heal (Playername) (Option) - Heal a player or yourself"));
                        player.sendTextMessage(TextFormat.Color("orange", " -> Options: all, hunger, thirst, bleeding, brokenbones,"));
                        player.sendTextMessage(TextFormat.Color("orange", " -> health, stamina"));
                        player.sendTextMessage(TextFormat.Color("yellow", "Page 2: /help 2"));
                    }
                    case 2 -> {
                        player.sendTextMessage(TextFormat.Color("orange", "-------- Help (2/3) --------"));
                        player.sendTextMessage(TextFormat.Color("orange", "/help (PageNr|Command) - Show the help"));
                        player.sendTextMessage(TextFormat.Color("orange", " -> Command: time, weather"));
                        player.sendTextMessage(TextFormat.Color("orange", "/kick <Playernamer> <Reason> - Kicks a player from the server"));
                        player.sendTextMessage(TextFormat.Color("orange", "/kickall <Reason> - Kicks all players from the server"));
                        player.sendTextMessage(TextFormat.Color("orange", "/near - Shows the player who is closest"));
                        player.sendTextMessage(TextFormat.Color("orange", "/playerinfo <Playername> - Show the player info"));
                        player.sendTextMessage(TextFormat.Color("orange", "/players - Show a list of all players with they groups"));
                        player.sendTextMessage(TextFormat.Color("orange", "/spawn (Playername) - Teleport you to the server spawnpoint"));
                        player.sendTextMessage(TextFormat.Color("orange", "/sudo <Command> - Execute a Command as Console"));
                        player.sendTextMessage(TextFormat.Color("orange", "/time <h> <min> - Change time"));
                        player.sendTextMessage(TextFormat.Color("orange", "/time <Typ> - Change time (Type: See /help time)"));
                        player.sendTextMessage(TextFormat.Color("orange", "/tp <Player> <Player> - Teleport the player to the other player"));
                        player.sendTextMessage(TextFormat.Color("yellow", "Page 3: /help 3"));
                    }
                    case 3 -> {

                        player.sendTextMessage(TextFormat.Color("orange", "-------- Help (3/3) --------"));
                        player.sendTextMessage(TextFormat.Color("orange", "/tp <Player> <X> <Y> <Z> - Teleport the player to the coordinate"));
                        player.sendTextMessage(TextFormat.Color("orange", "/tp <X> <Y> <Z> - Teleport you to the coordinate"));
                        player.sendTextMessage(TextFormat.Color("orange", "/tp help - Show more"));
                        player.sendTextMessage(TextFormat.Color("orange", "/tphere <Playername> - Teleport a player to you"));
                        player.sendTextMessage(TextFormat.Color("orange", "/tpto <Playername> - Teleport you to the player"));
                        player.sendTextMessage(TextFormat.Color("orange", "/weather <Type> (Instant) - Change weather"));
                    }
                }
                player.sendTextMessage(TextFormat.Color("orange", "---------------------------"));
            } else {
                switch (f) {
                    case "time" -> {
                        if (player.isAdmin()) {
                            player.sendTextMessage(TextFormat.Color("orange", "-------- Help Time --------"));
                            player.sendTextMessage(TextFormat.Color("orange", "Change the time."));
                            player.sendTextMessage(TextFormat.Color("orange", "/time <h> <min>"));
                            player.sendTextMessage(TextFormat.Color("orange", "/time <Type>"));
                            player.sendTextMessage(TextFormat.Color("orange", "--- Types ---"));
                            for (String s : Elements.getTimeList()) {
                                player.sendTextMessage(TextFormat.Color("orange", "- " + s));
                            }
                            player.sendTextMessage(TextFormat.Color("orange", "---------------------------"));
                        }
                    }
                    case "weather" -> {
                        if (player.isAdmin()) {
                            player.sendTextMessage(TextFormat.Color("orange", "-------- Weather Time --------"));
                            player.sendTextMessage(TextFormat.Color("orange", "Change the weather."));
                            player.sendTextMessage(TextFormat.Color("orange", "/weather <Type>"));
                            player.sendTextMessage(TextFormat.Color("orange", "/weather <Type> <Instant>"));
                            player.sendTextMessage(TextFormat.Color("orange", "--- Types ---"));
                            for (String s : Elements.getWeatherList()) {
                                player.sendTextMessage(TextFormat.Color("orange", "- " + s));
                            }
                            player.sendTextMessage(TextFormat.Color("orange", "- true = Switch weather on"));
                            player.sendTextMessage(TextFormat.Color("orange", "- false = Switch weather off"));
                            player.sendTextMessage(TextFormat.Color("orange", "--- Instant ---"));
                            player.sendTextMessage(TextFormat.Color("orange", "- true"));
                            player.sendTextMessage(TextFormat.Color("orange", "- false"));
                            player.sendTextMessage(TextFormat.Color("orange", "------------------------------"));
                        }
                    }
                }
            }

        }
    }

    private Player getNearestPlayer(Player player) {
        float dmin = -1, dresult = -1;
        Player result = null;
        for (Player p : Server.getAllPlayers()) {
            if (!p.getUID().equals(player.getUID())) {
                dresult = player.getPosition().distance(p.getPosition());
                if (dmin == -1 || dresult < dmin) {
                    dmin = dresult;
                    result = p;
                }
            }
        }

        return result;
    }

}
