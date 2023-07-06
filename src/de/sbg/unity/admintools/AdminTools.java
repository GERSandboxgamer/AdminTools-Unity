package de.sbg.unity.admintools;

import de.sbg.unity.admintools.Events.PlayerEvents;
import de.sbg.unity.admintools.Events.PlayerTeleporterEvent;
import de.sbg.unity.admintools.Events.atInputEvent;
import de.sbg.unity.admintools.Objects.Afk;
import de.sbg.unity.admintools.Objects.Teleporters;
import de.sbg.unity.configmanager.ConfigData;
import de.sbg.unity.configmanager.ConfigManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import net.risingworld.api.Plugin;

public class AdminTools extends Plugin {

    private atConsole Console;
    public Teleporters teleporters;
    public Config config;
    public atAttribute attribute;
    public Afk afk;

    @Override
    public void onEnable() {
        Console = new atConsole(this);
        Console.sendInfo("Enabled");
        config = new Config(this);
        try {
            config.iniConfig();
        } catch (NullPointerException ex) {
            Console.sendErr("NullPointerException");
            Console.sendErr(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException | NumberFormatException ex) {
            Console.sendErr(ex.getMessage());
            ex.printStackTrace();
        }
        afk = new Afk(this, Console);
        this.attribute = new atAttribute(this);
        Console.sendDebug("Config", "Config = " + config);
        Console.sendInfo("Config", "Done!");
        try {
            Update up = new Update(this, "http://gs.sandboxgamer.de/downloads/Plugins/risingworld/unity/AdminTools/version.txt");
        } catch (IOException | URISyntaxException ioex) {
        }
        Console.sendInfo("Teleporters", "Load Teleporters form Database");
        try {
            teleporters = new Teleporters(this, Console);
            registerEventListener(new PlayerTeleporterEvent(this));
        } catch (SQLException ex) {
            if (config.Debug > 0) {
                Console.sendErr(ex.getMessage());
                ex.printStackTrace();
            }
        }
        registerEventListener(new PlayerEvents(this, Console));
        registerEventListener(new atInputEvent(this, Console));
        registerEventListener(afk);
        if (config.AutoAfk) {
            Console.sendInfo("AutoAfk", "Enabled");
        } else {
            Console.sendInfo("AutoAfk", "Not Enabled");
        }

    }

    @Override
    public void onDisable() {
        try {
            teleporters.Database.saveAllTeleporters();
            teleporters.Database.close();
        } catch (SQLException ex) {
            Console.sendErr("DB", "Can not save all Teleports to Database!");
        }
        Console.sendInfo("Disabled");
    }

    public class Config {

        private final ConfigManager ConfigManager;
        public boolean TeleporterUseOnlyAdmin, TeleporterCreateOnlyAdmin, Command_Spawn, WelcomeMessage_Enabled,
                Command_Players, Command_Back, Command_Clear, Command_Compass, AutoAfk, Command_Afk;
        public String WelcomeMessage;
        public int Debug, AFK_Time;

        private final AdminTools plugin;

        public Config(AdminTools plugin) {
            this.plugin = plugin;
            this.ConfigManager = (ConfigManager) plugin.getPluginByName("ConfigManager");
            Console.sendInfo("Config", "ConfigManager = " + ConfigManager);

        }

        public void iniConfig() throws IOException, NumberFormatException {
            if (ConfigManager != null) {

                ConfigData Data = ConfigManager.newConfig(plugin.getName(), plugin.getPath());
                Data.addCommend("# ======== AdminTools-Config ========");
                Data.addEmptyLine();
                Data.addCommend("# Debug Modus - 0 = off; 1 = on");
                Data.addSetting("Debug", 0);
                Data.addEmptyLine();
                Data.addCommend("# When is a player AFK (in seconds)");
                Data.addSetting("AutoAfk", false);
                Data.addSetting("AFK_Time", 120);
                Data.addEmptyLine();
                Data.addCommend("# -------- Teleporter --------");
                Data.addEmptyLine();
                Data.addCommend("#Doesn't include: /tphere, /tpto, /tp x y z & /tp [Player] [Player] ( = Only Admin)");
                Data.addEmptyLine();
                Data.addCommend("# USE Teleporter only as admin");
                Data.addSetting("TeleporterUseOnlyAdmin", true);
                Data.addCommend("# CREATE/REMOVE Teleporter only as admin");
                Data.addSetting("TeleporterCreateOnlyAdmin", true);
                Data.addEmptyLine();
                Data.addCommend("# -------- Commands --------");
                Data.addEmptyLine();
                Data.addCommend("# Commands only for admins");
                Data.addSetting("Command_Afk", true);
                Data.addSetting("Command_Back", true);
                Data.addSetting("Command_Clear", true);
                Data.addSetting("Command_Compass", true);
                Data.addSetting("Command_Players", true);
                Data.addSetting("Command_Spawn", true);
                Data.addEmptyLine();
                Data.addCommend("# -------- Welcome message --------");
                Data.addEmptyLine();
                Data.addCommend("# Send a welcome message when connecting to the server");
                Data.addCommend("# %PLAYER% = Add playername");
                Data.addCommend("# %SERVER% = Add servername");
                Data.addSetting("WelcomeMessage_Enabled", false);
                Data.addSetting("WelcomeMessage", "Welcome %PLAYER% to the %SERVER% server!");
                Data.CreateConfig();
                Console.sendInfo("Config", "Laod Settings");
                Debug = Integer.parseInt(Data.getSetting("Debug"));
                TeleporterUseOnlyAdmin = Boolean.parseBoolean(Data.getSetting("TeleporterUseOnlyAdmin"));
                TeleporterCreateOnlyAdmin = Boolean.parseBoolean(Data.getSetting("TeleporterCreateOnlyAdmin"));
                Command_Afk = Boolean.parseBoolean(Data.getSetting("Command_Afk"));
                Command_Back = Boolean.parseBoolean(Data.getSetting("Command_Back"));
                Command_Clear = Boolean.parseBoolean(Data.getSetting("Command_Clear"));
                Command_Compass = Boolean.parseBoolean(Data.getSetting("Command_Compass"));
                Command_Players = Boolean.parseBoolean(Data.getSetting("Command_Players"));
                Command_Spawn = Boolean.parseBoolean(Data.getSetting("Command_Spawn"));
                WelcomeMessage_Enabled = Boolean.parseBoolean(Data.getSetting("WelcomeMessage_Enabled"));
                WelcomeMessage = Data.getSetting("WelcomeMessage");
                AFK_Time = Integer.parseInt(Data.getSetting("AFK_Time"));
                AutoAfk = Boolean.parseBoolean(Data.getSetting("AutoAfk"));

                Console.sendInfo("Config", "Debug = " + Data.getSetting("Debug"));

                if (Debug > 0) {
                    Console.sendDebug("Config", "Debug = " + Debug);
                    Console.sendDebug("Config", "TeleporterUseOnlyAdmin = " + TeleporterUseOnlyAdmin);
                    Console.sendDebug("Config", "TeleporterCreateOnlyAdmin = " + TeleporterCreateOnlyAdmin);
                    Console.sendDebug("Config", "Command_Afk = " + Command_Afk);
                    Console.sendDebug("Config", "Command_Back = " + Command_Back);
                    Console.sendDebug("Config", "Command_Clear = " + Command_Clear);
                    Console.sendDebug("Config", "Command_Compass = " + Command_Compass);
                    Console.sendDebug("Config", "Command_Players = " + Command_Players);
                    Console.sendDebug("Config", "Command_Spawn = " + Command_Spawn);
                    Console.sendDebug("Config", "WelcomeMessage_Enabled = " + WelcomeMessage_Enabled);
                    Console.sendDebug("Config", "WelcomeMessage = " + WelcomeMessage);
                    Console.sendDebug("Config", "AFK_Time = " + AFK_Time);
                    Console.sendDebug("Config", "AutoAfk = " + AutoAfk);
                }
            }
        }

    }

}
