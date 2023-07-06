package de.sbg.unity.admintools.Events;

import de.sbg.unity.admintools.AdminTools;
import de.sbg.unity.admintools.TextFormat;
import de.sbg.unity.admintools.atConsole;
import net.risingworld.api.Server;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.general.InputEvent;
import net.risingworld.api.objects.Player;

public class atInputEvent implements Listener {

    private final AdminTools plugin;
    private final EventElements Elements;
    private final atConsole Console;
    private final TextFormat TextFormat;

    public atInputEvent(AdminTools plugin, atConsole Console) {
        this.plugin = plugin;
        this.Elements = new EventElements();
        this.TextFormat = new TextFormat();
        this.Console = Console;
    }

    private void sendInfo(Object msg) {
        Console.sendInfo("Command", msg);
    }

    private void sendWarning(Object msg) {
        Console.sendWarning("Command", msg);
    }

    private void sendError(Object msg) {
        Console.sendErr("Command", msg);
    }

    @EventMethod
    public void onInputEvent(InputEvent event) {
        String[] cmd = event.getInput().split(" ");
        Player player = null;

        if (cmd.length >= 2) {
            if (cmd[0].toLowerCase().equals("at")) {
                if (cmd.length == 2) {

                    if (cmd[1].toLowerCase().equals("help")) {
                        Help();
                    }
                    
                    if (cmd[1].toLowerCase().equals("weather")) {
                        sendInfo("Current weather: " + Server.getCurrentWeather().name);
                    }
                    
                    if (cmd[1].toLowerCase().equals("players")) {
                        if (Server.getPlayerCount() > 0) {
                            sendInfo("======== Player-List ========");
                            for (Player p2 : Server.getAllPlayers()) {
                                sendInfo("- " + p2.getName() + "(Group: " + p2.getPermissionGroup() + ")");
                            }
                            sendInfo("============ End ============");
                        } else {
                            sendInfo("No player online!");
                        }
                    }

                }
                if (cmd.length == 3) {
                    player = Server.getPlayerByName(cmd[2]);
                    if (cmd[1].toLowerCase().equals("heal")) {
                        if (player != null && player.isConnected()) {
                            player.setHealth(player.getMaxHealth());
                            player.setThirst(100);
                            player.setHunger(100);
                            player.setStamina(player.getMaxStamina());
                            sendInfo("Heal the player!");
                        } else {
                            sendInfo("Player not found!");
                        }
                    }
                }

                if (cmd.length == 4) {
                    player = Server.getPlayerByName(cmd[3]);
                    if (cmd[1].toLowerCase().equals("gm")) {

                        if (player != null && player.isConnected()) {
                            if (cmd[2].toLowerCase().equals("c") || cmd[2].toLowerCase().equals("creative") || cmd[2].equals("1")) {
                                player.setCreativeModeEnabled(true);
                            }
                            if (cmd[2].toLowerCase().equals("s") || cmd[2].toLowerCase().equals("survivel") || cmd[2].equals("0")) {
                                //FIXME player.setCreativeModeEnabled(false);
                                sendInfo("Command not working, because bug in the API!");
                                sendInfo("Please use in the console 'gm 0'");
                            }
                            player.sendTextMessage(TextFormat.Color("orange", "Change your gamemode by console!"));
                            Console.sendInfo("Command", "Cange players Gamemode");
                        } else {
                            Console.sendInfo("Command", "Player not found!");
                        }
                    }

//                    if (cmd[1].toLowerCase().equals("heal")) {
//                        
//                    }
//
//                    if (cmd[1].toLowerCase().equals("playerinfo")) {
//
//                    }
//
//                    if (cmd[1].toLowerCase().equals("spawn")) {
//
//                    }
//
//                    if (cmd[1].toLowerCase().equals("time")) {
//
//                    }
//
//                    if (cmd[1].toLowerCase().equals("weather")) {
//
//                    }
//
//                    if (cmd[1].toLowerCase().equals("")) {
//
//                    }
                }
            }
        }
    }

    private void Help() {
        sendInfo("at gm");
        sendInfo("at help");
        sendInfo("at heal");
        sendInfo("at players");
        sendInfo("at weather");
        
        
    }

}
