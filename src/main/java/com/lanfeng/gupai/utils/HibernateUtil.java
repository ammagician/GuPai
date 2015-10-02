package com.lanfeng.gupai.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	private static SessionFactory sf = null;
	
	static{
		sf = getSessionFactory();
	}
	
	
	private static SessionFactory getSessionFactory(){
		Configuration config = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		SessionFactory sf = config.buildSessionFactory(serviceRegistry);
		return sf;
	}
	
	public static Session getSession(){
		return sf.openSession();
	}
}
