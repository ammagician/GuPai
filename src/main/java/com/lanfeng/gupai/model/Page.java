package com.lanfeng.gupai.model;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	private int start;
	private int end;
	private int total;
	private int limit;
	private int currentPage;
	private int totalPage;
	private List<T> list = new ArrayList<T>();
	
	public Page() {
		// TODO Auto-generated constructor stub
	}

	public Page(int start, int end, int total, int limit, int currentPage,
			int totalPage, List<T> list) {
		super();
		this.start = start;
		this.end = end;
		this.total = total;
		this.limit = limit;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
		this.list = list;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	
}
