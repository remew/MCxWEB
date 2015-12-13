package net.remew.mods.mcxweb.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.remew.mods.mcxweb.JedisWrapper;

/**
 * Created by remew on 15/12/13.
 */
public class LogInOutEvent
{
    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        System.out.println(event.player.getDisplayName() + " was logged in.");
        JedisWrapper.addLoginUser(event.player.getDisplayName());
    }

    @SubscribeEvent
    public void playerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        System.out.println(event.player.getDisplayName() + " was logged out.");
        JedisWrapper.removeLoginUser(event.player.getDisplayName());
    }
}
