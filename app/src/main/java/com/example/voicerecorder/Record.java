package com.example.voicerecorder;

import java.util.Date;

public class Record {
    public String Name;
    public int Time;
    public Date date;
    public int Size;

    public Record(String name, int time, Date date, int size){
        Name = name;
        this.date = date;
        Size = size;
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public Date getDate() {
        return date;
    }

    public int getSize() {
        return Size;
    }

    public int getTime() {
        return Time;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSize(int size) {
        Size = size;
    }

    public void setTime(int time) {
        Time = time;
    }
}
