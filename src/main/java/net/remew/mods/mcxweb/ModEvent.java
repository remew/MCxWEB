package net.remew.mods.mcxweb;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by remew on 15/12/01.
 */
public class ModEvent
{
    @SubscribeEvent
    public void entityJoinWorldEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        String name = player.getDisplayName();
        //System.out.println(String.format("%sによるPlayerLoggedInEventの発火", name));
    }

    @SubscribeEvent
    public void entityLeaveWorldEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayer player = event.player;
        String name = player.getDisplayName();
        //System.out.println(String.format("%sによるPlayerLoggedOutEventの発火", name));
    }
}
