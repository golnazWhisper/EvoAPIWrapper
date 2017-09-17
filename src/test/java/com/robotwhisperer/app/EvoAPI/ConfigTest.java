package com.robotwhisperer.app.EvoAPI;

import org.junit.Test;

/**
 * Created by admin_golnaz on 9/16/2017.
 */
public class ConfigTest {

    @Test
    public void readConfigFile() throws Exception {
        Config  config = new Config();
        System.out.println(config.toString());
    }

}