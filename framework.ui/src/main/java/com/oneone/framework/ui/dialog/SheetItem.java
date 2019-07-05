package com.oneone.framework.ui.dialog;

public class SheetItem {
    private String value;

    private int id;

    private int arg0;

    public SheetItem(String name, int id) {
        this.value = name;
        this.id = id;
    }

    public SheetItem(String name, int id, int arg0) {
        this(name, id);
        this.arg0 = arg0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArg0() {
        return arg0;
    }

    public void setArg0(int arg0) {
        this.arg0 = arg0;
    }
}