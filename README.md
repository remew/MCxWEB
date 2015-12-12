# MCxWEB
This mod is for connect Minecraft Server and Web Technology.

## Future
This mod will...
1. fire redis's pub/sub when...
  - Started Server.
  - Stopped Server.
  - Join User.(not yet)
  - Left User.(not yet)
1. store who logged in the server.(not yet)
  - Key is 'LoginUsers' and value is list.

This mod publish json text on 'MCxWEB' channel'.
For example...
```
//when started server
channel: MCxWEB
//second key is value of "type".
message: "{\"type\": \"state\", \"state\": \"Started\"}"
```

## requirement
1.This mod want to Jedis Library. I use Jedis version 2.8.0.
1. This mod want to Redis(KVS). I debugged with Redis version 2.8.4.

## Special Thanks
Thanks for Jedis Library!!

