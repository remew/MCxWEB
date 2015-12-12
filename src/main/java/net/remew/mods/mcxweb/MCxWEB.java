package net.remew.mods.mcxweb;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

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

    private Jedis jedis;

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
        cfg.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        try
        {
            this.jedis = new Jedis(REDIS_HOST, REDIS_PORT, REDIS_TIMEOUT);
            if (!REDIS_PASS.equals(""))
            {
                this.jedis.auth(REDIS_PASS);
            }
            this.jedis.connect();
        }
        catch(JedisException e)
        {
            this.jedis = null;
            System.out.println("Warning!!! Can't connect to jedis!!!");
            if (REDIS_FORCE)
            {
                System.out.println("Server will shutdown because force mode!!!");
                throw e;
            }
            else
            {
                System.out.println("Server will not shutdown because not force mode!!!");
            }
        }
        MinecraftForge.EVENT_BUS.register(new ModEvent());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        this.updateState("Started");
        System.out.println("Server Started");
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        this.updateState("Stopped");
        this.jedis.close();
        System.out.println("Server Stopped");
    }

    private void updateState(String state)
    {
        String KEY = "state";
        if (this.jedis != null && this.jedis.isConnected())
        {
            this.jedis.set(KEY, state);
            this.jedis.publish("MCxWEB", this.generateKeyValueMap(KEY, state));
        }
    }
    private String generateKeyValueMap(String key, String value)
    {
        return String.format("{\"type\":\"%s\", \"%s\":\"%s\"}", key, key, value);
    }

    // Server only mod
    @NetworkCheckHandler
    public boolean netWorkHandler(Map<String, String> mods, Side side)
    {
        return side.isClient();
    }
}
