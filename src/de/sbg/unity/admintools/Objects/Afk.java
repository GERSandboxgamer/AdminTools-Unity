package de.sbg.unity.admintools.Objects;

import de.sbg.unity.admintools.AdminTools;
import de.sbg.unity.admintools.Events.PlayerStartAfkEvent;
import de.sbg.unity.admintools.Events.PlayerStopAfkEvent;
import de.sbg.unity.admintools.TextFormat;
import de.sbg.unity.admintools.atConsole;
import net.risingworld.api.Server;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerChangeEquippedItemEvent;
import net.risingworld.api.events.player.PlayerChangePositionEvent;
import net.risingworld.api.events.player.PlayerChatEvent;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerConsumeItemEvent;
import net.risingworld.api.events.player.PlayerCraftItemEvent;
import net.risingworld.api.events.player.PlayerCreateAreaEvent;
import net.risingworld.api.events.player.PlayerCreateBlueprintEvent;
import net.risingworld.api.events.player.PlayerDismountNpcEvent;
import net.risingworld.api.events.player.PlayerDrinkWaterEvent;
import net.risingworld.api.events.player.PlayerDropItemEvent;
import net.risingworld.api.events.player.PlayerDropItemFromStorageEvent;
import net.risingworld.api.events.player.PlayerElementInteractionEvent;
import net.risingworld.api.events.player.PlayerEnterAreaEvent;
import net.risingworld.api.events.player.PlayerEnterVehicleEvent;
import net.risingworld.api.events.player.PlayerLeaveAreaEvent;
import net.risingworld.api.events.player.PlayerMountNpcEvent;
import net.risingworld.api.events.player.PlayerNpcInteractionEvent;
import net.risingworld.api.events.player.PlayerNpcInventoryAccessEvent;
import net.risingworld.api.events.player.PlayerObjectInteractionEvent;
import net.risingworld.api.events.player.PlayerPickupItemEvent;
import net.risingworld.api.events.player.PlayerPlayerInteractionEvent;
import net.risingworld.api.events.player.PlayerPrivateMessageEvent;
import net.risingworld.api.events.player.PlayerRespawnEvent;
import net.risingworld.api.events.player.PlayerStartFishingEvent;
import net.risingworld.api.events.player.PlayerStartFlyingEvent;
import net.risingworld.api.events.player.PlayerStopFishingEvent;
import net.risingworld.api.events.player.PlayerStopFlyingEvent;
import net.risingworld.api.events.player.PlayerStorageAccessEvent;
import net.risingworld.api.events.player.PlayerTeleportEvent;
import net.risingworld.api.events.player.ui.PlayerChangeScreenResolutionEvent;
import net.risingworld.api.events.player.ui.PlayerToggleInventoryEvent;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.events.player.ui.PlayerUIInputTextEvent;
import net.risingworld.api.events.player.world.PlayerChangeConstructionColorEvent;
import net.risingworld.api.events.player.world.PlayerChangeObjectColorEvent;
import net.risingworld.api.events.player.world.PlayerChangeObjectInfoEvent;
import net.risingworld.api.events.player.world.PlayerChangeObjectStatusEvent;
import net.risingworld.api.events.player.world.PlayerCreativePlaceVegetationEvent;
import net.risingworld.api.events.player.world.PlayerCreativeRemoveConstructionEvent;
import net.risingworld.api.events.player.world.PlayerCreativeRemoveObjectEvent;
import net.risingworld.api.events.player.world.PlayerCreativeRemoveVegetationEvent;
import net.risingworld.api.events.player.world.PlayerCreativeTerrainEditEvent;
import net.risingworld.api.events.player.world.PlayerDestroyConstructionEvent;
import net.risingworld.api.events.player.world.PlayerDestroyObjectEvent;
import net.risingworld.api.events.player.world.PlayerDestroyVegetationEvent;
import net.risingworld.api.events.player.world.PlayerEditConstructionEvent;
import net.risingworld.api.events.player.world.PlayerHitConstructionEvent;
import net.risingworld.api.events.player.world.PlayerHitObjectEvent;
import net.risingworld.api.events.player.world.PlayerHitTerrainEvent;
import net.risingworld.api.events.player.world.PlayerHitVegetationEvent;
import net.risingworld.api.events.player.world.PlayerHitWaterEvent;
import net.risingworld.api.events.player.world.PlayerPlaceBlueprintEvent;
import net.risingworld.api.events.player.world.PlayerPlaceConstructionEvent;
import net.risingworld.api.events.player.world.PlayerPlaceGrassEvent;
import net.risingworld.api.events.player.world.PlayerPlaceObjectEvent;
import net.risingworld.api.events.player.world.PlayerPlaceTerrainEvent;
import net.risingworld.api.events.player.world.PlayerPlaceVegetationEvent;
import net.risingworld.api.events.player.world.PlayerPlaceWaterEvent;
import net.risingworld.api.events.player.world.PlayerRemoveConstructionEvent;
import net.risingworld.api.events.player.world.PlayerRemoveGrassEvent;
import net.risingworld.api.events.player.world.PlayerRemoveObjectEvent;
import net.risingworld.api.events.player.world.PlayerRemoveVegetationEvent;
import net.risingworld.api.events.player.world.PlayerRemoveWaterEvent;
import net.risingworld.api.objects.Player;

public class Afk implements Listener {

    private final AdminTools plugin;
    private final String AfkTimeStamp, isAfk;
    private final TextFormat TextFormat;

    private long afkTime;

    public Afk(AdminTools plugin, atConsole Console) {
        this.plugin = plugin;
        this.AfkTimeStamp = "sgb-AdminTools-Attribute-AfkTimeStamp";
        this.isAfk = "sgb-AdminTools-Attribute-isAfk";
        this.afkTime = plugin.config.AFK_Time * 1000;
        this.TextFormat = new TextFormat();

        if (plugin.config.AutoAfk) {
            plugin.registerEventListener(new AutoAfk(plugin, this, Console));
        }
    }

    public void setAfk(Player player, boolean afk) {
        player.setAttribute(isAfk, afk);
    }

    public boolean isAfk(Player player) {
        return (boolean) player.getAttribute(isAfk);
    }

    public void setAfkTime(long afkTime) {
        this.afkTime = afkTime;
    }

    public long getAfkConfigTimeMillis() {
        return afkTime;
    }

    public long getTimeStamp(Player player) {
        return (long) player.getAttribute(AfkTimeStamp);
    }

    public long newTimeStamp(Player player) {
        long ts = System.currentTimeMillis() + afkTime;
        player.setAttribute(AfkTimeStamp, ts);
        return ts;
    }

    @EventMethod
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        Player player = event.getPlayer();
        newTimeStamp(player);
    }

    @EventMethod
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCommandEvent(PlayerCommandEvent event) {
        if (!event.getCommand().contains("/afk")) {
            if (isAfk(event.getPlayer())) {
                plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
            } else {
                newTimeStamp(event.getPlayer());
            }
        }
    }

    @EventMethod
    public void onPlayerChangePositionEvent(PlayerChangePositionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerChatEvent(PlayerChatEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerChangeEquippedItemEvent(PlayerChangeEquippedItemEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerConsumeItemEvent(PlayerConsumeItemEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCraftItemEvent(PlayerCraftItemEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCreateAreaEvent(PlayerCreateAreaEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCreateBlueprintEvent(PlayerCreateBlueprintEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerDismountNpcEvent(PlayerDismountNpcEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerDrinkWaterEvent(PlayerDrinkWaterEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerDropItemFromStorageEvent(PlayerDropItemFromStorageEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerElementInteractionEvent(PlayerElementInteractionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerEnterAreaEvent(PlayerEnterAreaEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerEnterVehicleEvent(PlayerEnterVehicleEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerLeaveAreaEvent(PlayerLeaveAreaEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerMountNpcEvent(PlayerMountNpcEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerNpcInteractionEvent(PlayerNpcInteractionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerNpcInventoryAccessEvent(PlayerNpcInventoryAccessEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerObjectInteractionEvent(PlayerObjectInteractionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlayerInteractionEvent(PlayerPlayerInteractionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPrivateMessageEvent(PlayerPrivateMessageEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerStartFishingEvent(PlayerStartFishingEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerStartFlyingEvent(PlayerStartFlyingEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerStopFishingEvent(PlayerStopFishingEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerStopFlyingEvent(PlayerStopFlyingEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerStorageAccessEvent(PlayerStorageAccessEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerChangeConstructionColorEvent(PlayerChangeConstructionColorEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerChangeObjectColorEvent(PlayerChangeObjectColorEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerChangeObjectInfoEvent(PlayerChangeObjectInfoEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerChangeObjectStatusEvent(PlayerChangeObjectStatusEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCreativePlaceVegetationEvent(PlayerCreativePlaceVegetationEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCreativeRemoveConstructionEvent(PlayerCreativeRemoveConstructionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCreativeRemoveObjectEvent(PlayerCreativeRemoveObjectEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCreativeRemoveVegetationEvent(PlayerCreativeRemoveVegetationEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerCreativeTerrainEditEvent(PlayerCreativeTerrainEditEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerDestroyConstructionEvent(PlayerDestroyConstructionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerDestroyObjectEvent(PlayerDestroyObjectEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerDestroyVegetationEvent(PlayerDestroyVegetationEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerEditConstructionEvent(PlayerEditConstructionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerHitConstructionEvent(PlayerHitConstructionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerHitObjectEvent(PlayerHitObjectEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerHitTerrainEvent(PlayerHitTerrainEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerHitVegetationEvent(PlayerHitVegetationEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerHitWaterEvent(PlayerHitWaterEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlaceBlueprintEvent(PlayerPlaceBlueprintEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlaceConstructionEvent(PlayerPlaceConstructionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlaceGrassEvent(PlayerPlaceGrassEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlaceObjectEvent(PlayerPlaceObjectEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlaceTerrainEvent(PlayerPlaceTerrainEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlaceVegetationEvent(PlayerPlaceVegetationEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerPlaceWaterEvent(PlayerPlaceWaterEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerRemoveConstructionEvent(PlayerRemoveConstructionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerRemoveGrassEvent(PlayerRemoveGrassEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerRemoveObjectEvent(PlayerRemoveObjectEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerRemoveVegetationEvent(PlayerRemoveVegetationEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerRemoveWaterEvent(PlayerRemoveWaterEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerChangeScreenResolutionEvent(PlayerChangeScreenResolutionEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerToggleInventoryEvent(PlayerToggleInventoryEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerUIElementClickEvent(PlayerUIElementClickEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    @EventMethod
    public void onPlayerUIInputTextEvent(PlayerUIInputTextEvent event) {
        if (isAfk(event.getPlayer())) {
            plugin.triggerEvent(new PlayerStopAfkEvent(event.getPlayer(), newTimeStamp(event.getPlayer())));
        } else {
            newTimeStamp(event.getPlayer());
        }
    }

    //-----------------------------------------------------------------------------------
    @EventMethod
    public void onPlayerStartAfkvent(PlayerStartAfkEvent event) {
        Player player = event.getPlayer();
        setAfk(player, true);
        player.sendTextMessage(TextFormat.Color("orange", "You are now AFK!"));
        for (Player p2 : Server.getAllPlayers()) {
            if (!p2.getUID().equals(player.getUID())) {
                p2.sendTextMessage(TextFormat.Color("orange", "Player '" + player.getName() + "' is now AFK!"));
            }
        }
    }

    @EventMethod
    public void onPlayerStopAfkEvent(PlayerStopAfkEvent event) {
        Player player = event.getPlayer();
        setAfk(player, false);
        player.sendTextMessage(TextFormat.Color("orange", "You are not longer AFK!"));
        for (Player p2 : Server.getAllPlayers()) {
            if (!p2.getUID().equals(player.getUID())) {
                p2.sendTextMessage(TextFormat.Color("orange", "Player '" + player.getName() + "' is not longer AFK!"));
            }
        }
    }

}
