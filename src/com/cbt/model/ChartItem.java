/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.model;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Overloaded constructor showing polymorphism
 */
public class ChartItem implements Serializable {

    String[] label;
    Integer[] data;
    Boolean fill;
    String borderColor;
    String backgroundColor;

    private static final long serialVersionUID = 5L;

    /**
     * Overloaded constructor showing polymorphism
     */
    public ChartItem(Integer[] data, String backgroundColor) {
        this.data = data;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Overloaded constructor showing polymorphism
     */
    public ChartItem(String[] label, Integer[] data, Boolean fill, String borderColor) {
        this.label = label;
        this.data = data;
        this.fill = fill;
        this.borderColor = borderColor;
    }

    /**
     * Overriding the toString methods
     */
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
