package com.lanfeng.gupai.utils.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONObject implements Serializable {
	protected final static Log log = LogFactory.getLog(JSONObject.class);
	private static ObjectMapper mapper = new ObjectMapper();
	private Map<Object, Object> map = null;

	public JSONObject() {
		this.map = new HashMap<Object, Object>();
	}

	public JSONObject(Map<Object, Object> map) {
		this.map = map;
	}

	/**
	 * build jsonobject with id1, value1, id2, value2, id3, value3..., ignore
	 * the last if keysAndValues's len is odd
	 *
	 * @param keysAndvalues
	 */
	public JSONObject(Object... keysAndvalues) {
		this.map = new HashMap<Object, Object>();
		if (keysAndvalues != null && keysAndvalues.length >= 2) {
			int i = 1;
			while (i < keysAndvalues.length) {
				put(keysAndvalues[i - 1], keysAndvalues[i]);
				i = i + 2;
			}
		}
	}

	public static <Bean> String toJson(Bean bean) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(bean);
		} catch (Exception e) {
			log.error("fromObject, wrong JSON data format: " + bean);
		}
		return null;
	}

	public static <Bean> Bean toBean(String json, Class beanClass) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return (Bean) mapper.readValue(json, beanClass);
		} catch (Exception e) {

		}
		return null;
	}

	public static JSONObject fromObject(String data) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		try {
			map = mapper.readValue(data, Map.class);
		} catch (Exception e) {
			log.error("fromObject, wrong JSON data format: " + data);
		}

		return new JSONObject(map);
	}

	public static JSONObject fromObject(Map map) {
		return new JSONObject(map);
	}

	public void remove(String key) {
		map.remove(key);
	}

	public boolean missingParam(String... keys) {
		boolean rest = false;
		for (int i = 0, len = keys.length; i < len; i++) {
			if (!map.containsKey(keys[i])) {
				return true;
			}
		}
		return rest;
	}

	public void clear() {
		map = new HashMap<Object, Object>();
	}

	public JSONObject put(Object key, Object object) {
		if (object instanceof JSONArray) {
			map.put(key, ((JSONArray) object).getList());
		} else if (object instanceof JSONObject) {
			map.put(key, ((JSONObject) object).map);
		} else {
			map.put(key, object);
		}
		return this;
	}

	public void putAll(JSONObject jsonObj) {
		for (Entry<Object, Object> entry : jsonObj.getMap().entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	public String getString(Object key) {
		Object obj = map.get(key);
		if (obj != null) {
			return obj.toString();
		} else {
			return null;
		}
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public String getString(Object key, String defaultValue) {
		String value = getString(key);
		if (value == null || value.isEmpty()) {
			value = defaultValue;
		}

		return value;
	}

	public Date getDate(Object key) {
		Object obj = map.get(key);
		if (obj != null) {
			return DateUtil.parse(obj.toString());
		} else {
			return null;
		}
	}

	public Date getDate(Object key, String format) {
		Object obj = map.get(key);
		if (obj != null) {
			return DateUtil.parse(obj.toString(), format);
		} else {
			return null;
		}
	}

	public JSONArray getJSONArray(String key) {
		Object obj = map.get(key);
		if (obj instanceof List) {
			return new JSONArray((List) obj);
		} else if (obj instanceof String) {
			return JSONArray.fromObject((String) obj);
		} else {
			return null;
		}
	}

	public boolean getBoolean(String key) {
		Object obj = map.get(key);
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof String) {
			return obj.equals("true") || obj.equals("1");
		} else {
			return false;
		}
	}

	public double getDouble(String key) {
		Object obj = map.get(key);
		if (obj instanceof Double) {
			return (Double) obj;
		} else if (obj instanceof String) {
			String value = obj.toString();
			return DoubleUtil.parse(value);
		} else {
			return Double.NaN;
		}
	}

	public int getInt(String key) {
		Object obj = map.get(key);
		if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof Double) {
			return (int) (double) (Double) obj;
		} else if (obj instanceof String) {
			String value = obj.toString();
			return IntegerUtil.parse(value);
		} else {
			return 0;
		}
	}

	public long getLong(String key) {
		Object obj = map.get(key);
		if (obj instanceof Long) {
			return (Long) obj;
		} else if (obj instanceof Double) {
			return (long) (double) (Double) obj;
		} else if (obj instanceof String) {
			String value = obj.toString();
			return LongUtil.parse(value);
		} else {
			return 0;
		}
	}

	public JSONObject getJSONObject(String key) {
		Object obj = map.get(key);
		if (obj instanceof Map) {
			return new JSONObject((Map<Object, Object>) obj);
		}
		return null;
	}

	public int size() {
		return map == null ? 0 : map.size();
	}

	public boolean has(String key) {
		return map != null && map.get(key) != null;
	}

	public boolean containsKey(String key) {
		return map != null && map.containsKey(key);
	}

	@Override
	public String toString() {
		String result = null;
		try {
			result = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Set<Object> getKeys() {
		return map.keySet();
	}

	public Iterator keys() {
		return map.keySet().iterator();
	}

	public Map<Object, Object> getMap() {
		return map;
	}
}
