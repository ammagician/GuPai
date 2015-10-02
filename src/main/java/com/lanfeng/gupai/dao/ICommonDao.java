package com.lanfeng.gupai.dao;

import java.io.Serializable;
import java.util.List;

import com.lanfeng.gupai.model.Page;

public interface ICommonDao <T, PK extends Serializable>{
	public List<T> getALL(String tableName);
	public List<T> getALLByHql(String hql);
	public T get(T t, PK pk);
	public boolean delete(T t);
	public boolean batchDelete(List<T> ts);
	public boolean update(T t);
	public boolean batchUpdate(List<T> ts);
	public T add(T t);
	public void batchAdd(List<T> ts);
	public Page<T> getPage(Page<T> p, String tableName); 
}
