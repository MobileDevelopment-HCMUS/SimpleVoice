package com.example.voicerecorder;

import android.location.Location;
import android.location.LocationManager;

import java.io.Serializable;
import java.util.Date;

public class Record  implements Serializable {
    public String Name;
    public String Time;
    public String path;
    public Date date;
    public int Size;
    public double longitude;
    public double latitude;

    public Record(String name, String time, String path, Date date, int size){
        Name = name;
        this.date = date;
        this.path = path;
        Size = size;
        Time = time;

    }

    public String getName() {
        return Name;
    }

    public String getPath() { return path; }

    public Date getDate() {
        return date;
    }

    public int getSize() {
        return Size;
    }

    public String getTime() {
        return Time;
    }

    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }

    public void setName(String name) {
        Name = name;
    }

    public void setPath(String path) { this.path = path; }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSize(int size) {
        Size = size;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setLocation(double la, double lo) {
        longitude = lo;
        latitude = la;
    }
}
