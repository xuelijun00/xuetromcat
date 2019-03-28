package com.yks.bigdata.web.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FilterDto implements Serializable  {
	
	/**
	 * 过滤属性
	 */
	private String sidx;  //字段
	private String sord;   // 正序或倒序
	private String[] searchField;
	private String[] searchString;
	private String[] searchOper;
	private boolean _search;
	private int rows;    // 一页行数
	private int page;  //页数
	private int isGrid;  
	
	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String[] getSearchField() {
		return searchField;
	}

	public void setSearchField(String[] searchField) {
		this.searchField = searchField;
	}

	public String[] getSearchString() {
		return searchString;
	}

	public void setSearchString(String[] searchString) {
		this.searchString = searchString;
	}

	public String[] getSearchOper() {
		return searchOper;
	}

	public void setSearchOper(String[] searchOper) {
		this.searchOper = searchOper;
	}

	public boolean is_search() {
		return _search;
	}

	public void set_search(boolean _search) {
		this._search = _search;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getIsGrid() {
		return isGrid;
	}

	public void setIsGrid(int isGrid) {
		this.isGrid = isGrid;
	}

}
