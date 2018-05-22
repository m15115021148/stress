package com.meigsmart.slb767_stress.db;

/**
 * Created by chenMeng on 2018/4/26.
 */
public class FunctionBean {
    private int id;
    private String sName;
    private int results;//0 unTest ; 1  failure ; 2 success
    private String reason;//test reason
    private int sSelect;//0 unselect ;  1 select;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getsSelect() {
        return sSelect;
    }

    public void setsSelect(int sSelect) {
        this.sSelect = sSelect;
    }
}
