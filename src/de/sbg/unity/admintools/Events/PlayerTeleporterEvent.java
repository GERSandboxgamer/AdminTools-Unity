package de.sbg.unity.admintools.Events;

import de.sbg.unity.admintools.AdminTools;
import de.sbg.unity.admintools.Objects.Teleporter;
import de.sbg.unity.admintools.TextFormat;
import java.sql.SQLException;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.objects.Player;

public class PlayerTeleporterEvent implements Listener {

    private final AdminTools plugin;
    private final TextFormat TextFormat;
    private final EventElements Elements;

    public PlayerTeleporterEvent(AdminTools plugin) {
        this.plugin = plugin;
        this.TextFormat = new TextFormat();
        this.Elements = new EventElements();
    }

    @EventMethod
    public void onPlayerCommandEvent(PlayerCommandEvent event) {
        String[] cmd = event.getCommand().split(" ");
        Player player = event.getPlayer();

        if (!plugin.config.TeleporterUseOnlyAdmin || player.isAdmin()) {
            if (cmd.length == 1) {
                if (cmd[0].toLowerCase().matches("/tpl")) {
                    player.sendTextMessage(TextFormat.Color("orange", "====Warp-List===="));
                    for (Teleporter tel : plugin.teleporters.getTeleporterList()) {
                        player.sendTextMessage(TextFormat.Color("orange", "- " + tel.getID() + ": " + tel.getName()));
                    }
                    player.sendTextMessage(TextFormat.Color("orange", "========================"));
                }
            }

            if (cmd.length == 2) {
                if (cmd[0].toLowerCase().matches("/tp")) {
                    if (!cmd[1].toLowerCase().equals("help")) {
                        if (!plugin.config.TeleporterUseOnlyAdmin || player.isAdmin()) {
                            if (Elements.toNumber(cmd[1]) >= 0) {
                                Teleporter tel = plugin.teleporters.getTeleporterByID(Elements.toNumber(cmd[1]));
                                if (tel != null) {
                                    player.setPosition(tel.getPosition());
                                    player.setRotation(tel.getRotation());
                                    plugin.attribute.Player.setBackPosition(player, player.getPosition());
                                    player.sendTextMessage(TextFormat.Color("green", "Teleport you to '" + tel.getName() + "'!"));
                                } else {
                                    player.sendTextMessage(TextFormat.Color("red", "Teleporter '" + cmd[1] + "' doesn't exist!"));
                                }
                            } else {

                                if (plugin.teleporters.hasTeleporter(cmd[1])) {
                                    Teleporter tel = plugin.teleporters.getTeleporterByName(cmd[1]);
                                    player.setPosition(tel.getPosition());
                                    player.setRotation(tel.getRotation());
                                    plugin.attribute.Player.setBackPosition(player, player.getPosition());
                                    player.sendTextMessage(TextFormat.Color("green", "Teleport you to '" + cmd[1] + "'!"));
                                } else {
                                    player.sendTextMessage(TextFormat.Color("red", "Teleporter '" + cmd[1] + "' doesn't exist!"));
                                }
                            }
                        }
                    } else {
                        if (!plugin.config.TeleporterUseOnlyAdmin || player.isAdmin()) {
                            player.sendTextMessage(TextFormat.Color("orange", "------- Teleporter Help -------"));
                            player.sendTextMessage(TextFormat.Color("orange", "/tp [Name/Nr] - Teleport you to the teleporter"));
                            player.sendTextMessage(TextFormat.Color("orange", "/tpl - A list of all teleporter"));
                            if (!plugin.config.TeleporterCreateOnlyAdmin || player.isAdmin()) {
                                player.sendTextMessage(TextFormat.Color("orange", "/tpr [Name] - Remove a teleport"));
                                player.sendTextMessage(TextFormat.Color("orange", "/tps [Name] - Set or change a teleporter"));
                                player.sendTextMessage(TextFormat.Color("orange", "Note: To rename, please delete and recreate the teleporter!"));
                            }
                            player.sendTextMessage(TextFormat.Color("orange", "-------------------------------"));
                        }
                    }
                }
                if (!plugin.config.TeleporterCreateOnlyAdmin || player.isAdmin()) {
                    if (cmd[0].toLowerCase().matches("/tps")) {
                        if (plugin.teleporters.hasTeleporter(cmd[1])) {
                            try {
                                plugin.teleporters.Database.editTeleporter(cmd[1], player.getPosition(), player.getRotation());
                                player.sendTextMessage(TextFormat.Color("green", "Teleporter '" + cmd[1] + "' successfully changed!"));
                            } catch (SQLException ex) {
                                player.sendTextMessage(TextFormat.Color("red", "[DB] Teleporter could not be edited!"));
                            }

                        } else {
                            try {
                                if (plugin.teleporters.addNewTeleporter(cmd[1], player) != null) {
                                    player.sendTextMessage(TextFormat.Color("green", "Created teleporter '" + cmd[1] + "' successfully!"));
                                } else {
                                    player.sendTextMessage(TextFormat.Color("red", "Teleporter '" + cmd[1] + "' could not be created!"));
                                }
                            } catch (SQLException ex) {
                                player.sendTextMessage(TextFormat.Color("red", "[DB] Teleporter could not be created!"));
                            }
                        }
                    }
                    if (cmd[0].toLowerCase().matches("/tpr")) {
                        if (plugin.teleporters.hasTeleporter(cmd[1])) {
                            try {
                                plugin.teleporters.removeTeleporter(cmd[1]);
                                player.sendTextMessage(TextFormat.Color("green", "Teleporter '" + cmd[1] + "' successfully removed!"));
                            } catch (SQLException ex) {
                                player.sendTextMessage(TextFormat.Color("red", "[DB] Teleporter could not be removed!"));
                            }

                        }
                    }
                }

            }
        }
    }
}
