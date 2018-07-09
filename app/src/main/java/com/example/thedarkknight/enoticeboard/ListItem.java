package com.example.thedarkknight.enoticeboard;

/**
 * Created by The Dark Knight on 23-05-2018.
 */

public class ListItem {
    private String head;
    private String desc;
    private String type;
    private String college;

    public ListItem(String head, String desc, String type,String college) {
        this.head = head;
        this.desc = desc;
        this.type = type;
        this.college=college;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getCollege() {
        return college;
    }

}
