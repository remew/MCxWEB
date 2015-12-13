package net.remew.mods.mcxweb;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.remew.mods.mcxweb.events.LogInOutEvent;
import net.remew.mods.mcxweb.events.ChatEvent;

import java.util.Map;

@Mod(modid = MCxWEB.MODID, version = MCxWEB.VERSION)
public class MCxWEB
{
    public static final String MODID = "mcxweb";
    public static final String VERSION = "0.0.1";

    public static int REDIS_PORT;
    public static int REDIS_TIMEOUT;
    public static String REDIS_HOST;
    public static String REDIS_PASS;
    public static boolean REDIS_FORCE;
    public static int MAX_SAVE_CHAT;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        cfg.load();
        REDIS_HOST = cfg.getString("RedisHostAddress", "Redis", "localhost", "Redis's host address.");
        REDIS_PORT = cfg.getInt("RedisHostPort", "Redis", 6379, 1024, 65535, "Redis's host port");
        REDIS_PASS = cfg.getString("RedisHostPassword", "Redis", "", "Redis's host address.");
        REDIS_TIMEOUT = cfg.getInt("RedisConnectionTimeout", "Redis", 2000, 1, 10000, "Redis's connection timeout miliseccond");
        REDIS_FORCE = cfg.getBoolean("ForceUsingRedis", "Redis", false, "If true and can't connect to redis, shutdown server");
        MAX_SAVE_CHAT = cfg.getInt("MaxSaveChat", "Settings", 100, 0, 65535, "Max store chat number.");
        cfg.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        JedisWrapper.setRedisHost(REDIS_HOST);
        JedisWrapper.setRedisPort(REDIS_PORT);
        JedisWrapper.setRedisTimeout(REDIS_TIMEOUT);
        JedisWrapper.setRedisPass(REDIS_PASS);
        MinecraftForge.EVENT_BUS.register(new ChatEvent());
        FMLCommonHandler.instance().bus().register(new LogInOutEvent());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        System.out.println("Server Started");
        JedisWrapper.connect();
        JedisWrapper.updateServerState("Started");
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        System.out.println("Server Stopped");
        JedisWrapper.updateServerState("Stopped");
        JedisWrapper.disconnect();
    }

    // Server only mod
    @NetworkCheckHandler
    public boolean netWorkHandler(Map<String, String> mods, Side side)
    {
        return side.isClient();
    }//*/
}
