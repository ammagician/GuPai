package com.lanfeng.gupai.cacheCenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import redis.clients.jedis.Jedis;

import com.lanfeng.gupai.model.scence.Desk;
import com.lanfeng.gupai.model.scence.Hall;
import com.lanfeng.gupai.model.scence.Room;
import com.lanfeng.gupai.utils.common.SerializeUtil;

public class CacheCenter{
	private static RedisServer server;
	static{
		server = RedisServer.getInstance();
	}
	
	public static void removeString(String key){
		Jedis redis = server.getRedis();
		redis.del(key);
		server.returnRedis(redis);
	}
	
	public static void setString(String key, String value){
		Jedis redis = server.getRedis();
		redis.set(key, value);
		server.returnRedis(redis);
	}
	
	public static String getString(String key){
		Jedis redis = server.getRedis();
		String val = redis.get(key);
		server.returnRedis(redis);
		return val;
	}
	
	public static void removeObject(String key){
		Jedis redis = server.getRedis();
		redis.del(key.getBytes());
		server.returnRedis(redis);
	}
	
	public static <T extends Serializable> void setObject(String key, T obj){
		Jedis redis = server.getRedis();
		redis.set(key.getBytes(), SerializeUtil.serialize(obj));
		server.returnRedis(redis);
	}
	
	public static Object getObject(String key){
		Jedis redis = server.getRedis();
		byte[] bytes = redis.get(key.getBytes());
		server.returnRedis(redis);
		return SerializeUtil.deSerialize(bytes);
	}
	
	public static List<Object> getObjectsFromSet(String key){
		Jedis redis = server.getRedis();
		Iterator<byte[]> iter = redis.smembers(key.getBytes()).iterator();
		server.returnRedis(redis);
		List<Object> result = new ArrayList<Object>();
		while (iter.hasNext()) {
			byte[] r = iter.next();
			result.add(SerializeUtil.deSerialize(r));
		}
		
		return result;
	}
	
	public static <T extends Cachable> void setObjectToSet(String key, T t){
		Jedis redis = server.getRedis();
		redis.sadd(key.getBytes(), SerializeUtil.serialize(t)); 
		server.returnRedis(redis);
	}
	
	public static <T extends Cachable> void replaceObject(String key, T o , T n){
		Jedis redis = server.getRedis();
		redis.srem(key.getBytes(), SerializeUtil.serialize(o));
		redis.sadd(key.getBytes(), SerializeUtil.serialize(n)); 
		server.returnRedis(redis);
	}
	
	public static <T extends Cachable> void delObjectFromSet(String key, T t){
		Jedis redis = server.getRedis();
		redis.srem(key.getBytes(), SerializeUtil.serialize(t));
		server.returnRedis(redis);
	}
	
	public static void setHall(Hall hall){
		setObject(hall.getId(), hall);
	}
	
	public static Hall getHall(String hallId){
		return (Hall)getObject(hallId);
	}
	
	public static void setRoom(Room room){
		setObject(room.getId(), room);
	}
	
	public static Room getRoom(String roomId){
		return (Room)getObject(roomId);
	}
	
	public static void setRooms(String hallId, Room room){
		setObjectToSet(hallId, room);
	}
	
	public static void setRooms(String hallId, List<Room> rooms){
		for(Room r : rooms){
			setObjectToSet(hallId, r);
		}
	}
	
	public static List<Room> getRooms(String hallId){
		List<Object> objs = getObjectsFromSet(hallId);
		List<Room> rooms = new ArrayList<Room>();
		for(Object o : objs){
			rooms.add((Room) o);
		}
		return rooms;
	}
	
	public static void setDesk(Desk desk){
		setObject(desk.getId(), desk);
	}
	
	public static Desk getDesk(Desk desk){
		return (Desk)getObject(desk.getId());
	}
	
	public static List<Desk> getDesks(String roomId){
		List<Object> objs = getObjectsFromSet(roomId);
		List<Desk> desks = new ArrayList<Desk>();
		for(Object o : objs){
			desks.add((Desk) o);
		}
		return desks;
	}
	
	public static void setDesk(String roomId, Desk desk){
		setObjectToSet(roomId, desk);
	}
	
	public static void delDesk(String roomId, Desk desk){
		delObjectFromSet(roomId, desk);
	}
	
	public static void setDesks(String roomId, List<Desk> desks){
		for(Desk d : desks){
			setObjectToSet(roomId, d);
		}
	}

}
