package com.lanfeng.gupai.cacheCenter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisServer {
	private JedisPool pool;
	private static RedisServer server;
	
	public static RedisServer getInstance(){
		if(server == null){
			server = new RedisServer();
		}
		return server;
	}
	
	private RedisServer(){
		init();
	}
	
	private void init(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(500);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000*60*30);
		config.setTestOnBorrow(true);
		pool = new JedisPool(config, "127.0.0.1", 6379);
		
		Jedis jedis = pool.getResource();
		jedis.flushDB();
		jedis.close();
	}
	
	public Jedis getRedis (){
		return pool.getResource();
	}
	
	public void returnRedis (Jedis jedis){
		jedis.close();
	}
	
}
