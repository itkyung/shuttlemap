package com.shuttlemap.web;

import com.google.gson.annotations.Expose;

public class DatatableJson {
	@Expose
	private int draw ;     // 현재 페이지
	@Expose
	private int recordsTotal;   // 전체 레코드
	@Expose
	private int recordsFiltered;     // 전체 페이지
	@Expose
	private Object[] data;
	@Expose
	private int start;
	@Expose
	private int limit;
	
	public DatatableJson(int draw, int recordsTotal, int recordsFiltered, Object[] rows) {
		this.draw = draw;
		this.recordsTotal = recordsTotal;
		//this.recordsFiltered = recordsFiltered;
		this.recordsFiltered = recordsTotal; //filterered의 의미가 없다.
		this.data = rows;
		this.start = 0;
		this.limit = 0;
	}

	public DatatableJson(int draw, int recordsTotal, int recordsFiltered, Object[] rows,int start,int limit) {
		this.draw = draw;
		this.recordsTotal = recordsTotal;
		//this.recordsFiltered = recordsFiltered;
		this.recordsFiltered = recordsTotal; //filterered의 의미가 없다.
		this.data = rows;
		this.start = start;
		this.limit = limit;
	}
	
	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}
	
	
	
	
}
