package org.example.apigateway.utils;

public class TokenBucketLuaScript {
    public static final String SCRIPT =
            "local rate     = tonumber(ARGV[1])\n"
                    + "local capacity = tonumber(ARGV[2])\n"
                    + "local now      = tonumber(ARGV[3])\n"
                    + "local key      = KEYS[1]\n"
                    + "\n"
                    + "-- Fetch current bucket state (tokens, timestamp)\n"
                    + "local data  = redis.call('HMGET', key, 'tokens', 'timestamp')\n"
                    + "local tokens = tonumber(data[1])\n"
                    + "local last   = tonumber(data[2])\n"
                    + "if tokens == nil then tokens = capacity; last = now end\n"
                    + "\n"
                    + "-- Refill according to elapsed time (1 000 ms interval)\n"
                    + "local delta  = math.max(0, now - last)\n"
                    + "local refill = math.floor(delta / 1000) * rate\n"
                    + "if refill > 0 then\n"
                    + "  tokens = math.min(tokens + refill, capacity)\n"
                    + "  last   = now\n"
                    + "end\n"
                    + "\n"
                    + "-- Try to consume one token\n"
                    + "local allowed = 0\n"
                    + "if tokens > 0 then\n"
                    + "  tokens  = tokens - 1\n"
                    + "  allowed = 1\n"
                    + "end\n"
                    + "\n"
                    + "-- Persist new state and TTL (slightly > 1 s so idle buckets expire)\n"
                    + "redis.call('HMSET', key, 'tokens', tokens, 'timestamp', last)\n"
                    + "redis.call('PEXPIRE', key, 2000)\n"
                    + "return { allowed, tokens }";
}

