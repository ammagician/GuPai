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
	
	public static List<Object> getObjects(String key){
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
	
	public static <T extends Cachable> void setObjects(String key, T t){
		Jedis redis = server.getRedis();
		redis.sadd(key.getBytes(), SerializeUtil.serialize(t)); 
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
		setObjects(hallId, room);
	}
	
	public static void setRooms(String hallId, List<Room> rooms){
		for(Room r : rooms){
			setObjects(hallId, r);
		}
	}
	
	public static List<Room> getRooms(String hallId){
		List<Object> objs = getObjects(hallId);
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
		List<Object> objs = getObjects(roomId);
		List<Desk> desks = new ArrayList<Desk>();
		for(Object o : objs){
			desks.add((Desk) o);
		}
		return desks;
	}
	
	public static void setDesks(String roomId, Desk desk){
		setObjects(roomId, desk);
	}
	
	public static void setDesks(String roomId, List<Desk> desks){
		for(Desk d : desks){
			setObjects(roomId, d);
		}
	}

}
