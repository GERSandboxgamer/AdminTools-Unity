
package de.sbg.unity.admintools.Events;

import net.risingworld.api.events.Event;
import net.risingworld.api.objects.Player;


public class PlayerStartAfkEvent extends Event{
    
    private final Player player;
    private final long TimeStamp;
    
    public PlayerStartAfkEvent(Player player, long TimeStamp){
        this.TimeStamp = TimeStamp;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

}
