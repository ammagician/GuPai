package com.lanfeng.gupai.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.lanfeng.gupai.dao.ICommonDao;
import com.lanfeng.gupai.model.Page;

public class CommonDao<T, PK extends Serializable> implements ICommonDao<T, PK> {
	private Session getSession(){
		SessionFactory sf = new Configuration().configure().buildSessionFactory(null);
		Session s = sf.openSession();
		return s;
	}

	public T get(T t, PK pk) {
		Session s = getSession();
		s.byId(t.getClass()).load(pk);
		return null;
	}
	
	public List<T> getALL(String tableName) {
		Session s = getSession();
		String hql = "from " + tableName;
		List<T> list = null;
		Query q = s.createQuery(hql);
		list = (List<T>)q.list();
		return list;
	}
	
	public Page<T> getPage(Page<T> p, String tableName) {
		int curPage = p.getCurrentPage();
		int limit = p.getLimit();
		
		Session s = getSession();
		String hql = "from " + tableName;
		SQLQuery tq = s.createSQLQuery("select count(*) from " + tableName);
		int total = ((Number)tq.uniqueResult()).intValue();
		int start = (curPage - 1) * limit;
		Query q = s.createQuery(hql);
		q.setFirstResult(start);
		q.setMaxResults(limit);
		List<T> list = (List<T>)q.list();
		int end = start + list.size();
		int totalPage = total / limit + 1;
		
		Page<T> rp = new Page<T>(start, end, total, limit, curPage,
				totalPage, list);
		return rp;
	}

	public boolean delete(T t) {
		Session s = getSession();
		Transaction tx = s.beginTransaction();
		s.delete(t);
		tx.commit();
		s.close();
		return true;
	}

	public boolean update(T t) {
		Session s = getSession();
		Transaction tx = s.beginTransaction();
		s.update(t);
		tx.commit();
		s.close();
		return true;
	}

	public T add(T t) {
		Session s = getSession();
		Transaction tx = s.beginTransaction();
		s.save(t);
		tx.commit();
		s.close();
		return t;
	}

	public boolean batchDelete(List<T> ts) {
		Session s = getSession();
		Transaction tx = s.beginTransaction();
		int i = 0;
		for ( T t : ts ) {
		    s.delete(t);
		    if ( i % 20 == 0 ) { //20, same as the JDBC batch size
		        //flush a batch of inserts and release memory:
		        s.flush();
		        s.clear();
		    }
		    i++;
		}
		tx.commit();
		s.close();
		return true;
	}


	public boolean batchUpdate(List<T> ts) {
		Session s = getSession();
		Transaction tx = s.beginTransaction();
		int i = 0;
		for ( T t : ts ) {
		    s.update(t);
		    if ( i % 20 == 0 ) { //20, same as the JDBC batch size
		        //flush a batch of inserts and release memory:
		        s.flush();
		        s.clear();
		    }
		    i++;
		}
		tx.commit();
		s.close();
		return false;
	}
}
