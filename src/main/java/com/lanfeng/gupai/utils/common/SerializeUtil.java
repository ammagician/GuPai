package com.lanfeng.gupai.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeUtil{			
	public static Object deSerialize(byte[] bytes){		
		Object obj = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public static <T extends Serializable>byte[] serialize(T obj){		
		ByteArrayOutputStream bis = new ByteArrayOutputStream();
		ObjectOutputStream ois;
		try {
			ois = new ObjectOutputStream(bis);
			ois.writeObject(obj);
			return bis.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
