/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.userdao;

import dao.DataBaseConnectionHandler;
import dao.userdao.UserDao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.User;

/**
 *
 * @author Aya
 */
public class UserDaoImp implements UserDao {

    DataBaseConnectionHandler dBConnection;
    PreparedStatement preparedStatement;

    public UserDaoImp() {
        dBConnection = DataBaseConnectionHandler.getInstance();
    }

    @Override
    public User register(String userName, String email, String password) {
        String insertUserQuery = "INSERT INTO " + utilities.UserTableConstants.USER_TABLE_NAME + " ("
                + utilities.UserTableConstants.USER_NAME_COLUMN + ", "
                + utilities.UserTableConstants.USER_EMAIL_COLUMN + ", "
                + utilities.UserTableConstants.USER_PASSWORD_COLUMN + ") VALUES (?,?,?)";
        System.out.println(insertUserQuery);
        User registeredUser = null;
        long generatedKey = 0;
        try {
            String generatedColumns[] = {utilities.UserTableConstants.USER_ID_COLUMN};
            preparedStatement = dBConnection.getConnection().prepareStatement(insertUserQuery, generatedColumns);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    generatedKey = rs.getLong(1);
                }
                registeredUser = new User();
                registeredUser.setId(generatedKey);
                registeredUser.setUserName(userName);
                registeredUser.setEmail(email);
                registeredUser.setPassword(password);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dBConnection.close();
        }
        return registeredUser;
    }

    @Override
    public User login(String userName, String password) {
        String selectUserQuery = "SELECT * FROM "
                + utilities.UserTableConstants.USER_TABLE_NAME + " WHERE "
                + utilities.UserTableConstants.USER_NAME_COLUMN + " =? AND "
                + utilities.UserTableConstants.USER_PASSWORD_COLUMN + " =? ";
        User loggedUser = null;
        try {
            preparedStatement = dBConnection.getConnection().prepareStatement(selectUserQuery);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                loggedUser = new User();
                loggedUser.setId(result.getLong(utilities.UserTableConstants.USER_ID_COLUMN));
                loggedUser.setUserName(result.getString(utilities.UserTableConstants.USER_NAME_COLUMN));
                loggedUser.setEmail(result.getString(utilities.UserTableConstants.USER_EMAIL_COLUMN));
                loggedUser.setPassword(result.getString(utilities.UserTableConstants.USER_PASSWORD_COLUMN));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dBConnection.close();
        }
        return loggedUser;
    }

    @Override
    public User registerWithGmail(String userName, String email) {
        //check if user already registered in database
        String selectUserQuery = "SELECT * FROM "
                + utilities.UserTableConstants.USER_TABLE_NAME + " WHERE "
                + utilities.UserTableConstants.USER_NAME_COLUMN + " =? AND "
                + utilities.UserTableConstants.USER_EMAIL_COLUMN + " =? ";
        User loggedUser = null;
        try {
            preparedStatement = dBConnection.getConnection().prepareStatement(selectUserQuery);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, email);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                // if user is in the database return user's data
                loggedUser = new User();
                loggedUser.setId(result.getLong(utilities.UserTableConstants.USER_ID_COLUMN));
                loggedUser.setUserName(result.getString(utilities.UserTableConstants.USER_NAME_COLUMN));
                loggedUser.setEmail(result.getString(utilities.UserTableConstants.USER_EMAIL_COLUMN));
                loggedUser.setPassword(result.getString(utilities.UserTableConstants.USER_PASSWORD_COLUMN));

            } else {
                // if user not in the database add him 
                String insertUserQuery = "INSERT INTO " + utilities.UserTableConstants.USER_TABLE_NAME + " ("
                        + utilities.UserTableConstants.USER_NAME_COLUMN + ", "
                        + utilities.UserTableConstants.USER_EMAIL_COLUMN + ", "
                        + utilities.UserTableConstants.USER_PASSWORD_COLUMN + ") VALUES (?,?,?)";
                long generatedKey = 0;
                String password = "123";
                String generatedColumns[] = {utilities.UserTableConstants.USER_ID_COLUMN};
                preparedStatement = dBConnection.getConnection().prepareStatement(insertUserQuery, generatedColumns);
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);

                int registerationResult = preparedStatement.executeUpdate();
                if (registerationResult > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        generatedKey = rs.getLong(1);
                    }
                    loggedUser = new User();
                    loggedUser.setId(generatedKey);
                    loggedUser.setUserName(userName);
                    loggedUser.setEmail(email);
                    loggedUser.setPassword(password);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dBConnection.close();
        }
        return loggedUser;
    }

}
