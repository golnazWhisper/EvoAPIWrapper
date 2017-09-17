package com.robotwhisperer.app.EvoAPI;

import com.javonet.Javonet;
import com.javonet.JavonetFramework;
import com.javonet.api.NObject;
import org.junit.Test;

/**
 * Created by admin_golnaz on 9/16/2017.
 */

public class EvaluateJavONet {

    @Test
    public void testJavOnet() throws Exception {

        Javonet.activate("golnaz@robotwhispererllc.com", "s8DX-o2E3-c3KR-Yd78-p2CH", JavonetFramework.v45);

        Javonet.addReference("Interop.Evoapi.dll");

        NObject evoSystem = Javonet.New("SystemClass");

        String userName = "Admin";
        String password = "tecan";
        int iMode = 0;
        int bSimulation = 0;
        evoSystem.invoke("Logon", userName, password, iMode, bSimulation);
        System.out.println("Invoked Logon");
        System.out.println("Invoking GetStatus");
        System.out.println(evoSystem.invoke("GetStatus"));
        evoSystem.invoke("Logoff");
        System.out.println("Invoked Logoff");
    }
}
