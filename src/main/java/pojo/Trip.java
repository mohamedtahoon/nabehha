/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.util.ArrayList;

/**
 *
 * @author Ahmed moatasem
 */
public class Trip {

    private String tripName;
    private String startPoint;
    private String endPoint;
    private ArrayList<TripNotes> notes;

    public ArrayList<TripNotes> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<TripNotes> notes) {
        this.notes = notes;
    }
    private String date;
    private String time;
    private String status;
    private String type;
    private String tripImage;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTripImage(String tripImage) {
        this.tripImage = tripImage;
    }
    private int id;

    public Trip() {
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

  

  
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTripImage() {
        return tripImage;
    }

}
