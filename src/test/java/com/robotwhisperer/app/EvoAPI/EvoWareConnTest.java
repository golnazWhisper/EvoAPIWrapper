package com.robotwhisperer.app.EvoAPI;

import org.junit.Test;

/**
 * Created by admin_golnaz on 9/16/2017.
 */
public class EvoWareConnTest {
    @Test
    public void statusChanged() throws Exception {
    }

    @Test
    public void prepareProcess() throws Exception {
    }

    @Test
    public void startProcess() throws Exception {
    }

    @Test
    public void prepareScript() throws Exception {
    }

    @Test
    public void startScript() throws Exception {
    }

    @Test
    public void startEVOware() throws Exception {
        EvoWareConn evoWareConn = new EvoWareConn();
        assert (evoWareConn.StartEVOware() == true);
    }

}