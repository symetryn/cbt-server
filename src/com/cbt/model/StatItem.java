/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Symetryn
 */
public class StatItem implements Serializable {
    private ArrayList<ChartItem> chartList;
    private String [] labelList;
    
    private static final long serialVersionUID = 6L;
    
    public StatItem(){
        chartList= new ArrayList();
    }

    public StatItem( String[] labelList,ArrayList<ChartItem> chartList) {
        this.chartList = chartList;
        this.labelList = labelList;
    }
    
    

    public ArrayList<ChartItem> getChartList() {
        return chartList;
    }

    public void setChartList(ArrayList<ChartItem> chartList) {
        this.chartList = chartList;
    }

    public String[] getLabelList() {
        return labelList;
    }

    public void setLabelList(String[] labelList) {
        this.labelList = labelList;
    }

  
    
    
}
