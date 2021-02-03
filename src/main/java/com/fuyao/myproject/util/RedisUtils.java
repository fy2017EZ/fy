package com.fuyao.myproject.util;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/1/27 14:06
 */
public class RedisUtils {

    private static ShardedJedisPool jedisPool = null;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        // 设置最大对象数
        jedisPoolConfig.setMaxTotal(20);

        // 最大能够保持空闲状态的对象数
        jedisPoolConfig.setMaxIdle(10);

        // 超时时间
        jedisPoolConfig.setMaxWaitMillis(10000);

        // 在获取连接的时候检查有效性, 默认false
        jedisPoolConfig.setTestOnBorrow(true);

        // 在返回Object时, 对返回的connection进行validateObject校验
        jedisPoolConfig.setTestOnReturn(true);

        // 如果是集群，可以全部加入list中
        List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>();
        JedisShardInfo shardInfo = new JedisShardInfo("127.0.0.1", 6379);
//        shardInfo.setPassword("redis123.");
//        shardInfos.add(shardInfo);

        jedisPool = new ShardedJedisPool(jedisPoolConfig, shardInfos);

    }

    /**
     * 从连接池中获取一个ShardedJedis对象
     */
    public synchronized  static ShardedJedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 关闭ShardedJedis对象，放回池中
     */
    public static void closeJedis(ShardedJedis jedis) {
        jedis.close();
    }

    /**
     * 通过key获取String类型Value
     *
     * @param key 键
     * @return 值
     */
    public synchronized  static String get(String key) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {

            return jedis.get(key);
        }
    }

    /**
     * 通过key获取字节数组类型Value
     *
     * @param key 键
     * @return 值
     */
    public synchronized  static byte[] get(byte[] key) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {

            return jedis.get(key);
        }
    }

    /**
     * 设置String类型key和value
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public synchronized  static String set(String key, String value) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {

            return jedis.set(key, value);
        }

    }

    /**
     * 设置字节数组类型key和value
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public synchronized  static String set(byte[] key, byte[] value) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {

            return jedis.set(key, value);
        }

    }

    /**
     * 删除指定key
     */
    public static Long del(String key) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {
            return jedis.del(key);
        }
    }

    /**
     * 左侧放入集合
     *
     * @param key 键
     * @param values 值集合
     * @return
     */
    public static Long lpush(String key, String... values) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {
            return jedis.lpush(key, values);
        }
    }

    /**
     * 左侧弹出一个元素
     *
     * @param key 指定键
     * @return 左侧第一个元素
     */
    public static String lpop(String key) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {
            return jedis.lpop(key);
        }
    }

    /**
     * 右侧放入集合
     *
     * @param key 键
     * @param values 值集合
     * @return
     */
    public static Long rpush(String key, String... values) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {
            return jedis.rpush(key, values);
        }
    }

    /**
     * 右侧弹出一个元素
     *
     * @param key 指定键
     * @return 右侧第一个元素
     */
    public static String rpop(String key) {
        try (
                ShardedJedis jedis = jedisPool.getResource();
        ) {
            return jedis.rpop(key);
        }
    }

}
