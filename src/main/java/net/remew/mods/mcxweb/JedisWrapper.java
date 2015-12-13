package net.remew.mods.mcxweb;

import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by remew on 15/12/13.
 */
public class JedisWrapper
{
    private static JedisWrapper instance;
    private static Jedis jedis;

    public static int REDIS_PORT;
    public static int REDIS_TIMEOUT;
    public static String REDIS_HOST;
    public static String REDIS_PASS;

    public static void setRedisHost(String host)
    {
        REDIS_HOST = host;
    }
    public static void setRedisPass(String pass)
    {
        REDIS_PASS = pass;
    }
    public static void setRedisTimeout(int timeout)
    {
        REDIS_TIMEOUT = timeout;
    }
    public static void setRedisPort(int port)
    {
        REDIS_PORT = port;
    }
    public static void connect()
    {
        if (JedisWrapper.jedis != null) throw new RuntimeException("Jedis instance is already connected");
        JedisWrapper.jedis = new Jedis(REDIS_HOST, REDIS_PORT, REDIS_TIMEOUT);
        if (!REDIS_PASS.equals(""))
        {
            JedisWrapper.jedis.auth(REDIS_PASS);
        }
        JedisWrapper.jedis.connect();
    }

    public static void disconnect()
    {
        if (JedisWrapper.jedis == null) throw new RuntimeException("Jedis instance is already disconnected");
        JedisWrapper.jedis.del(KEY_LOGIN_USERS);
        JedisWrapper.jedis.quit();
        JedisWrapper.jedis = null;
    }

    private static final String KEY_LOGIN_USERS = "LoginUsers";
    private static final String KEY_CHAT = "Chat";
    private static final String KEY_STATE = "State";

    public static void addLoginUser(String name)
    {
        JedisWrapper.jedis.sadd(KEY_LOGIN_USERS, name);
        JedisWrapper.jedis.publish("Login", name);
    }

    public static void removeLoginUser(String name)
    {
        JedisWrapper.jedis.srem(KEY_LOGIN_USERS, name);
        JedisWrapper.jedis.publish("Logout", name);
    }

    public static void addChatMessage(String name, Date date, String message)
    {
        String text = name + " " + new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(date) + " " + message;
        JedisWrapper.jedis.lpush(KEY_CHAT, text);
        JedisWrapper.jedis.ltrim(KEY_CHAT, 0, MCxWEB.MAX_SAVE_CHAT - 1);
        JedisWrapper.jedis.publish(KEY_CHAT, text);
    }

    public static void updateServerState(String state)
    {
        JedisWrapper.jedis.set(KEY_STATE, state);
        JedisWrapper.jedis.publish(KEY_STATE, state);
    }
}
