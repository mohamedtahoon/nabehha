/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.userdao;

import pojo.User;


/**
 *
 * @author Aya
 */
public interface UserDao {
    
    public User register (String userName, String email,  String password);
    public User registerWithGmail (String userName, String email);
    public User login (String userName, String password);
    
}
