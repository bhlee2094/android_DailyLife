package com.bhlee.dailylife;

public class DailyList extends MyItem {
    private String listTitle;
    private String listId;
    private String masterId;

    public DailyList() {
    }

    public DailyList(String listTitle, String listId, String masterId) {
        this.listTitle = listTitle;
        this.listId = listId;
        this.masterId = masterId;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }
}
