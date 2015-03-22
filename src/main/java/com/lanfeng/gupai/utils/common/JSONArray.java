package com.lanfeng.gupai.utils.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONArray {
	protected final static Log log = LogFactory.getLog(JSONObject.class);
	private static ObjectMapper mapper = new ObjectMapper();
	private List list = null;

	public JSONArray() {
		this.list = new ArrayList<Object>();
	}

	public JSONArray(List list) {
		this.list = list;
	}

	List getList() {
		return this.list;
	}

	public static JSONArray fromObject(String data) {
		List<Object> list = null;
		try {
			list = mapper.readValue(data, List.class);
		} catch (Exception e) {
			log.error("fromObject, wrong JSON data format: " + data);
		}

		return new JSONArray(list);
	}

	public static JSONArray fromObject(List list) {
		return new JSONArray(list);
	}

	public void clear() {
		list = new ArrayList<Object>();
	}

	public JSONObject getJSONObject(int index) {
		if (index >= 0 && index < list.size()) {
			Object obj = list.get(index);
			if (obj instanceof JSONObject) {
				return (JSONObject) obj;
			}
			return new JSONObject((Map) obj);
		} else {
			return null;
		}
	}

	public JSONArray getJSONArray(int index) {
		if (index >= 0 && index < list.size()) {
			return new JSONArray((List) list.get(index));
		} else {
			return null;
		}
	}

	public String getString(int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index).toString();
		} else {
			return null;
		}
	}

	public double getDouble(int index) {
		double result = Double.NaN;
		if (index >= 0 && index < list.size()) {
			// return list.get(index);
			Object value = list.get(index);
			if (value instanceof Double) {
				result = (Double) value;
			}
		}
		return result;
	}

	public void add(Object object) {
		if (object instanceof JSONArray) {
			list.add(((JSONArray) object).getList());
		} else if (object instanceof JSONObject) {
			list.add(((JSONObject) object).getMap());
		} else {
			list.add(object);
		}
	}

	public void add(int index, Object object) {
		if (object instanceof JSONArray) {
			list.add(index, ((JSONArray) object).getList());
		} else if (object instanceof JSONObject) {
			list.add(index, ((JSONObject) object).getMap());
		} else {
			list.add(index, object);
		}
	}

	public JSONArray addAll(List<JSONObject> objList) {
		for (JSONObject object : objList) {
			list.add(object.getMap());
		}
		return this;
	}

	public void set(int index, Object object) {
		if (index < 0 || index >= list.size()) {
			return;
		}
		if (object instanceof JSONArray) {
			list.set(index, ((JSONArray) object).getList());
		} else if (object instanceof JSONObject) {
			list.set(index, ((JSONObject) object).getMap());
		} else {
			list.set(index, object);
		}
	}

	public List toList() {
		return list;
	}

	public int size() {
		return list.size();
	}

	@Override
	public String toString() {
		String result = null;
		try {
			result = mapper.writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
