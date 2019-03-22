package tripservice;

import dao.tripdao.TripDaoImp;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Trip;
import pojo.TripNotes;

@Path("/trip")
public class TripService {

    TripDaoImp tripDaoImp;

    public TripService() {
        tripDaoImp = new TripDaoImp();
    }

    
    @POST
    @Path("/addTrip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Long addTrip(Trip trip,ArrayList<TripNotes> tripNotes) {
        Long tripId = tripDaoImp.addTrip(trip,tripNotes);
        return tripId;
    }
    
    
    @POST
    @Path("/addNote")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean addNote(TripNotes tripNotes,@QueryParam("tripId")String tripId) {
        boolean isTripAdd = tripDaoImp.addNote(tripNotes,Integer.parseInt(tripId));
        return isTripAdd;
    }
    
    @POST
    @Path("/getTrip")
    @Produces(MediaType.APPLICATION_JSON)
    public Trip getTripDetails(@QueryParam("tripId") String tripId) {
        Trip resultTrip = tripDaoImp.getTrip(Integer.parseInt(tripId));
        return resultTrip;
    }

    @GET
    @Path("/getTrips")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Trip> getTrips(@QueryParam("userId") String userId, @QueryParam("tripStatus") String type) {
        ArrayList<Trip> resultTrips = tripDaoImp.getTrips(Long.parseLong(userId), type);
        return resultTrips;
    }

    @POST
    @Path("/updatTripStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean upateStatus(@QueryParam("tripId") String tripId, @QueryParam("tripType") String type) {
        return tripDaoImp.updateStatus(Integer.parseInt(tripId), type);
    }

    @POST
    @Path("/deleteTrip")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteTrip(@QueryParam("tripId") String tripId) {
        return tripDaoImp.deleteFromTrip(Integer.parseInt(tripId));
    }

    @POST
    @Path("/deleteAllTrip")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteAllTrip(@QueryParam("userId") String userId) {
        return tripDaoImp.deleteUserTrips(Integer.parseInt(userId));
    }
}
