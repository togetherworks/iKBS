package com.ncs.ikbs.vo;

import java.util.TreeMap;
import java.util.Iterator;

public class AppListVO {

    private int nodeId;
    private String nodeName;
    private TreeMap applications;

    public AppListVO() {
        this.applications = new TreeMap();
    }

    
    public TreeMap getApplications() {
        return this.applications;
    }
    
    public void addApplication(int appId, String appName) {
        this.applications.put(appName, "" + appId);
    }
    
    public Iterator applicationsIterator() {
        return this.applications.keySet().iterator();
    }
    
    public String getApplicationId(String appName) {
        return (String)this.applications.get(appName);
    }
        
    public int getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}