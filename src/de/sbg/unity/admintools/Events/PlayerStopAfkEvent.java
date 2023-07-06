
package de.sbg.unity.admintools.Events;

import net.risingworld.api.events.Event;
import net.risingworld.api.objects.Player;

public class PlayerStopAfkEvent extends Event{
    private final Player player;
    private final long NewTimeStamp;
    
    public PlayerStopAfkEvent(Player player, long NewTimeStamp){
        this.NewTimeStamp = NewTimeStamp;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public long getNewTimeStamp() {
        return NewTimeStamp;
    }
}
