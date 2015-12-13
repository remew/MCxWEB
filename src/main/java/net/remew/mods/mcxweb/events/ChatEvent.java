package net.remew.mods.mcxweb.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.remew.mods.mcxweb.JedisWrapper;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Set;

/**
 * Created by remew on 15/12/01.
 */
public class ChatEvent
{
    @SubscribeEvent
    public void onChatEvent(ServerChatEvent event)
    {
        Date date = new Date();
        System.out.println("\"" + event.username + "\" says " + event.message);
        JedisWrapper.addChatMessage(event.username, date, event.message);
    }
}
