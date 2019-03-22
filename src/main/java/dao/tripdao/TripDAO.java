/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.tripdao;

import java.util.ArrayList;
import pojo.Trip;
import pojo.TripNotes;

/**
 *
 * @author Tahoon
 */
public interface TripDAO {

    public Long addTrip(Trip trip,ArrayList<TripNotes> tripNotes);

    public boolean deleteFromTrip(int tripId);

    public boolean deleteUserTrips(int userId);

    public boolean updateStatus(int tripId, String status);

    public ArrayList<Trip> getTrips(Long userId, String tripStatus);

    public Trip getTrip(int tripId);
    boolean addNote(TripNotes note,int tripId);
}
