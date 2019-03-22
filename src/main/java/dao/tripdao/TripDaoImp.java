/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.tripdao;

import dao.DataBaseConnectionHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.Trip;
import pojo.TripNotes;
import pojo.User;

/**
 *
 * @author abdullah
 */
public class TripDaoImp implements TripDAO {

    DataBaseConnectionHandler connection;
    long lastTripId = 0;

    @Override
    public Long addTrip(Trip trip, ArrayList<TripNotes> tripNotes) {
        try {
            connection = DataBaseConnectionHandler.getInstance();

            PreparedStatement pst = connection.getConnection().prepareStatement("insert into TRIP ("
                    + "TRIP_NAME,"
                    + "START_POINT,"
                    + "END_POINT,"
                    + "TRIP_DATE,"
                    + "TRIP_TIME,"
                    + "TRIP_TYPE,"
                    + "TRIP_IMAGE,"
                    + "USER_ID,"
                    + "TRIP_STATUS)"
                    + "Values (?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, trip.getTripName());
            pst.setString(2, trip.getStartPoint());
            pst.setString(3, trip.getEndPoint());
            pst.setString(4, trip.getDate());
            pst.setString(5, trip.getTime());
            pst.setString(6, trip.getType());
            pst.setString(7, trip.getTripImage());
            pst.setLong(8, trip.getUserId());
            pst.setString(9, trip.getStatus());
            int executeUpdate = pst.executeUpdate();

//            if (executeUpdate > 0) {
//
//                ResultSet rs = pst.getGeneratedKeys();
//                if (rs.next()) {
//                    System.out.println("id " + rs.getLong(1));;
////                    generatedKey = 
//                }
//            }
            pst.close();
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement("select " + utilities.TripTableConstants.TRIP_ID_COLUMN + " from " + utilities.TripTableConstants.TRIP_TABLE_NAME + " where " + utilities.TripTableConstants.TRIP_USER_ID_COLUMN + " = " + trip.getUserId() + " ORDER BY " + utilities.TripTableConstants.TRIP_ID_COLUMN + " DESC ");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastTripId = resultSet.getLong(1);
            }

            if (tripNotes != null) {
                if (!tripNotes.isEmpty()) {
                    PreparedStatement ps = connection.getConnection().prepareStatement("INSERT INTO TRIP_NOTES VALUES (?, ?)");
                    for (int i = 0; i < tripNotes.size(); i++) {
                        ps.setLong(1, lastTripId);
                        ps.setString(2, tripNotes.get(i).getNote());
                        ps.addBatch();
                        ps.clearParameters();

                    }
                    ps.executeBatch();
                }
            }
            connection.close();
            if (executeUpdate > 0) {
                return lastTripId;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TripDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1L;

    }

    //delete trip from "TRIP" by trip id 
    @Override
    public boolean deleteFromTrip(int tripId) {
        connection = DataBaseConnectionHandler.getInstance();
        PreparedStatement pst;
        try {
            pst = connection.getConnection().prepareStatement("delete From Trip where Trip_id=?");
            pst.setInt(1, tripId);
            int executeUpdate = pst.executeUpdate();
            pst.close();
            connection.close();
            if (executeUpdate > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TripDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // delete user Tripsss by user id 
    @Override
    public boolean deleteUserTrips(int userId) {
        connection = DataBaseConnectionHandler.getInstance();
        PreparedStatement pst;

        try {
            pst = connection.getConnection().prepareStatement("delete From TRIP where user_id=?");

            pst.setInt(1, userId);
            int executeUpdate = pst.executeUpdate();
            pst.close();
            connection.close();
            if (executeUpdate > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TripDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    //update Status To "Done or End" 
    @Override
    public boolean updateStatus(int tripId, String status) {
        connection = DataBaseConnectionHandler.getInstance();
        PreparedStatement pst;
        try {
            pst = connection.getConnection().prepareStatement("update TRIP set TRIP_STATUS=? where trip_id=?");
            pst.setString(1, status);
            pst.setInt(2, tripId);

            int executeUpdate = pst.executeUpdate();
            pst.close();
            connection.close();
            if (executeUpdate > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TripDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public ArrayList<Trip> getTrips(Long userId, String tripStatus) {
        connection = DataBaseConnectionHandler.getInstance();
        PreparedStatement pst;
        ResultSet resultSet;
        ArrayList<Trip> trips = new ArrayList<>();
        try {
            pst = connection.getConnection().prepareStatement("select * from TRIP.TRIP where TRIP.USER_ID =?  and  TRIP_STATUS =?");
            pst.setLong(1, userId);
            pst.setString(2, tripStatus);

            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                Trip trip = new Trip();
                trip.setTripName(resultSet.getString(utilities.TripTableConstants.TRIP_NAME_COLUMN));
                trip.setStartPoint(resultSet.getString(utilities.TripTableConstants.TRIP_START_POINT_COLUMN));
                trip.setEndPoint(resultSet.getString(utilities.TripTableConstants.TRIP_END_POINT_COLUMN));
                trip.setDate(resultSet.getString(utilities.TripTableConstants.TRIP_DATE_COLUMN));
                trip.setStatus(resultSet.getString(utilities.TripTableConstants.TRIP_STATUS_COLUMN));
                trip.setTime(resultSet.getString(utilities.TripTableConstants.TRIP_TIME_COLUMN));
                trip.setType(resultSet.getString(utilities.TripTableConstants.TRIP_TYPE_COLUMN));
                trip.setTripImage(resultSet.getString(utilities.TripTableConstants.TRIP_IMAGE_COLUMN));
                trips.add(trip);
            }
            pst.close();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(TripDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trips;

    }

    @Override
    public boolean addNote(TripNotes note, int tripId) {

        try {
            PreparedStatement pst = connection.getConnection().prepareStatement("insert into TRIP_NOTES("
                    + "TRIP_ID,"
                    + "NOTE"
            );
            pst.setInt(1, tripId);
            pst.setString(2, note.getNote());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TripDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Trip getTrip(int tripId) {
        connection = DataBaseConnectionHandler.getInstance();
        PreparedStatement pst;
        ResultSet resultSet;
        Trip trip = new Trip();

        try {
            pst = connection.getConnection().prepareStatement("select * from TRIP.TRIP where TRIP.Trip_id =?;");
            pst.setInt(1, tripId);

            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                trip.setTripName(resultSet.getString(resultSet.getString(utilities.TripTableConstants.TRIP_NAME_COLUMN)));
                trip.setStartPoint(resultSet.getString(utilities.TripTableConstants.TRIP_START_POINT_COLUMN));
                trip.setEndPoint(resultSet.getString(utilities.TripTableConstants.TRIP_END_POINT_COLUMN));
                trip.setDate(resultSet.getString(utilities.TripTableConstants.TRIP_DATE_COLUMN));
                trip.setStatus(resultSet.getString(utilities.TripTableConstants.TRIP_STATUS_COLUMN));
                trip.setTime(resultSet.getString(utilities.UserTableConstants.USER_NAME_COLUMN));
                trip.setType(resultSet.getString(utilities.TripTableConstants.TRIP_TYPE_COLUMN));
                trip.setTripImage(resultSet.getString(utilities.TripTableConstants.TRIP_IMAGE_COLUMN));
            }
            pst.close();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(TripDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trip;
    }

}
