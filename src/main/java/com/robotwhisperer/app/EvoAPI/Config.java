package com.robotwhisperer.app.EvoAPI;

/**
 * Created by admin_golnaz on 9/16/2017.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private String userName;
    private String password;
    private int mode;
    private boolean simulation;

    public Config() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = Config.class.getResourceAsStream("config.properties");
            prop.load(input);

            // get the property value and save them
            userName = prop.getProperty("userName");
            password = prop.getProperty("password");
            mode = Integer.parseInt(prop.getProperty("mode"));
            simulation = Boolean.parseBoolean(prop.getProperty("simulation"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean getSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    @Override
    public String toString() {
        return "Config{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", mode=" + mode +
                ", simulation=" + simulation +
                '}';
    }
}