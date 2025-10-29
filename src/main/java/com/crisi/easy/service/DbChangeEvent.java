package com.crisi.easy.service;

import java.io.Serializable;

public class DbChangeEvent implements Serializable {
    private String table;
    private String operation;
    private Object payload;

    public DbChangeEvent(String table, String operation, Object payload) {
        this.table = table;
        this.operation = operation;
        this.payload = payload;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}