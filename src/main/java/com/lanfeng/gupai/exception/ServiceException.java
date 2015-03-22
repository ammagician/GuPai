package com.lanfeng.gupai.exception;

public class ServiceException extends GPException {
	private static final long serialVersionUID = 1969545012437025804L;

	public ServiceException(Type type) {
		super(type);
	}

	public ServiceException(Type type, String message) {
		super(type, message);
	}

	public ServiceException(Type type, Throwable t) {
		super(type, t);
	}

	public ServiceException(Type type, String message, Throwable t) {
		super(type, message, t);
	}
}
