/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import utilities.Constants;


/**
 *
 * @author anas
 */
public class DataBaseConnectionHandler {
    private static final ArrayList<DataBaseConnectionHandler> INSTANCES = new ArrayList<>();
    private static final int MAX_CONCURRENCY_USERS = 10;
    private int currentUsageNumber;
    private Connection con;

    /**
     * @this currentUsageNumber for calculate free space of usage.
     */
    private DataBaseConnectionHandler(){
        try {
            this.currentUsageNumber = 0;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", Constants.DATABASE_NAME, Constants.DATABASE_PASSWORD);
            
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This Abdo'sTon extends Singleton, give single object for @field
     * MaxConcurrencyUser , create new object when first get full.
     * @return DatabaseConnection
     */
    public static DataBaseConnectionHandler getInstance() {
        if (INSTANCES.isEmpty()) {
            synchronized (DataBaseConnectionHandler.class) {
                if (INSTANCES.isEmpty()) {
                    DataBaseConnectionHandler temp = new DataBaseConnectionHandler();
                    INSTANCES.add(temp);
                }
            }
        } else {
            for (DataBaseConnectionHandler inst : INSTANCES) {
                if (inst.currentUsageNumber < MAX_CONCURRENCY_USERS) {
                    inst.currentUsageNumber++;
                    return inst;
                }
            }
            DataBaseConnectionHandler temp = new DataBaseConnectionHandler();
            INSTANCES.add(temp);
            temp.currentUsageNumber++;
            return temp;
        }
        //double check for space too
        if (INSTANCES.get(0).currentUsageNumber < MAX_CONCURRENCY_USERS) {
            INSTANCES.get(0).currentUsageNumber++;
            return INSTANCES.get(0);
        } else {
            return getInstance();
        }
    }

    /**
     * @this isEmptySpace just for ensure from when remove all object keep at
     * lest one has space to use
     *
     * This else if for give the next object info about the previous that it has
     * space
     */
    public static void removeNotInUse() {
        boolean isEmptySpace = false;
        Iterator<DataBaseConnectionHandler> it = INSTANCES.iterator();
        if (it.next().currentUsageNumber < MAX_CONCURRENCY_USERS) {
            isEmptySpace = true;//to skip first item /* It's singletone must keep one object ,maaaaaan */
        }
        for (; it.hasNext();) {
            DataBaseConnectionHandler inst = it.next();
            if (inst.currentUsageNumber == 0 && isEmptySpace) {
                try {
                    inst.getConnection().close();
                    INSTANCES.remove(inst);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else if (inst.currentUsageNumber < MAX_CONCURRENCY_USERS) {
                isEmptySpace = true;
            }
        }
    }

    /**
     * @return Connection
     * @this con , give you ability to operate DB.
     */
    public Connection getConnection() {
        return con;
    }
    
    public void close() {
        currentUsageNumber--;
        if(currentUsageNumber>=0){
            removeNotInUse();
        }
    }

    
}
