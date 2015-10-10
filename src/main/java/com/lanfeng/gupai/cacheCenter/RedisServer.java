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
		// ����һ��pool�ɷ�����ٸ�jedisʵ����ͨ��pool.getResource()����ȡ��
		// �����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��
		config.setMaxTotal(500);
		// ����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����
		config.setMaxIdle(5);
		// ��ʾ��borrow(����)һ��jedisʵ��ʱ�����ĵȴ�ʱ�䣬��������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��
		config.setMaxWaitMillis(1000*60*30);
		// ��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�
		config.setTestOnBorrow(true);
		pool = new JedisPool(config, "127.0.0.1", 6379);
	}
	
	public Jedis getRedis (){
		return pool.getResource();
	}
	
	public void returnRedis (Jedis jedis){
		jedis.close();
	}
	
}
