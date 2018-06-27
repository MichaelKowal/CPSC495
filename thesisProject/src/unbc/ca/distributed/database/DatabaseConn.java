/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IDontKnow
 */
public class DatabaseConn {

    private Connection con;
    ConcurrentHashMap<Integer, Query> queries = new ConcurrentHashMap<>();
    private boolean threadStarted = false;
    private int counter = 1;
    int threadLiveness = 1;
    boolean breakTheLoop = true;

    public DatabaseConn(String host, String port, String database, String username, String password) {
        if (!"".equals(port)) {
            port = ":" + port;
        } else {
            port = "";
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + port + "/" + database, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: I got it"+ ex);
            //Logger.getLogger(DatabaseConn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void executeQuery(final String query) {
        Query q = new Query();
        q.setId(counter);
        q.setQuery(query);
        queries.put(counter, q);
        counter++;
        if (!threadStarted) {
            executeQueries();
            threadStarted = true;
        }
    }

    private void executeQueries() {
        new Thread() {
            @Override
            public void run() {
                super.setName("Database Query Executor");
                while (breakTheLoop) {
                    if (queries.size() > 0) {
                        try {

                            for (Map.Entry<Integer, Query> entry : queries.entrySet()) {
                                Integer index = entry.getKey();
                                Query query = entry.getValue();
                                if (!query.isCompleted()) {
                                    PreparedStatement statement = con.prepareStatement(query.getQuery());
                                    if (!statement.execute()) {
                                        query.setCompleted(true);
                                        queries.remove(index);
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(DatabaseConn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        threadLiveness++;
                        //System.out.println("Value of the counter is ---> "+threadLiveness);
                        if(threadLiveness > 10000)
                        {
                            breakTheLoop = false;
                        }
                    }
                }
            }
        }.start();
    }
}
