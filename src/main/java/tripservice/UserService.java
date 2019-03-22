package tripservice;




import dao.userdao.UserDaoImp;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.User;

@Path("/user")
public class UserService {

    UserDaoImp userDaoImp;

    public UserService() {
        userDaoImp = new UserDaoImp();
    }

    @GET
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public User register(@QueryParam("userName") String userName, @QueryParam("email") String email, @QueryParam("password") String password) {
        User resultuser = userDaoImp.register(userName, email, password);
        if (resultuser == null) {
            throw new RegisterationFailedException("Database conflict");
        }
        return resultuser;
    }
    
       @POST
    @Path("/registerWithGmail")
    @Produces(MediaType.APPLICATION_JSON)
    public User registerWithGmail(@QueryParam("userName") String userName, @QueryParam("email") String email) {
        User resultuser = userDaoImp.registerWithGmail(userName, email);
        if (resultuser == null) {
            throw new RegisterationFailedException("Database conflict");
        }
        return resultuser;
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public User login(@QueryParam("userName") String userName, @QueryParam("password") String password) {
        User loggedUser = userDaoImp.login(userName, password);
        if (loggedUser == null) {
            throw new UserNotFoundException("User not found");
        }
        return loggedUser;
    }

}
