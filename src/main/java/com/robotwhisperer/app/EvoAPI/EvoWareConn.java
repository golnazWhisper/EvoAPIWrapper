package com.robotwhisperer.app.EvoAPI;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by admin_golnaz on 9/16/2017.
 */
public class EvoWareConn {

    private static Logger logger;
    private EvoAPISystem evoAPISystem;
    private EvoAPIenums.SC_Status currentStatus;

    public EvoWareConn() throws Exception  {
        logger = Logger.getLogger(EvoWareConn.class.getName());
        evoAPISystem = new EvoAPISystem();
    }

    private boolean WaitStatusChange(EvoAPIenums.SC_Status newStatus) throws Exception {
        // make sure the status has changed, it might not be instantaneous
        Thread.sleep(10000);
        try {
            StatusChanged(evoAPISystem.GetStatus());
            do {
                Thread.yield();
                Thread.sleep(100);
                StatusChanged(evoAPISystem.GetStatus());
                if ((currentStatus.value() & EvoAPIenums.SC_Status.STATUS_ERROR.value()) == EvoAPIenums.SC_Status.STATUS_ERROR.value())
                    return false;
            } while ((currentStatus.value() & newStatus.value()) == newStatus.value());
            return true;
        } catch (Exception e)  {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    private boolean WaitProcessStatus(int iProcessID) throws Exception {
        // make sure the status has changed, it might not be instantaneous
        try {
            EvoAPIenums.SC_ProcessStatus currentProcessStatus;
            do {
                Thread.yield();
                Thread.sleep(100);
                currentProcessStatus = evoAPISystem.GetProcessStatus(iProcessID);
                if (currentProcessStatus == EvoAPIenums.SC_ProcessStatus.PS_ERROR)
                    return false;
            } while (currentProcessStatus.value() != EvoAPIenums.SC_ProcessStatus.PS_FINISHED.value());
            return true;
        } catch (Exception e)  {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    private boolean WaitScriptStatus(int iScriptID) throws Exception {
        // make sure the status has changed, it might not be instantaneous
        try {
            EvoAPIenums.SC_ScriptStatus currentScriptStatus;
            do {
                Thread.yield();
                Thread.sleep(100);
                currentScriptStatus = evoAPISystem.GetScriptStatus(iScriptID);
                if(currentScriptStatus == EvoAPIenums.SC_ScriptStatus.SS_ERROR)
                    return false;
            } while (currentScriptStatus.value() != EvoAPIenums.SC_ScriptStatus.SS_IDLE.value());
            return true;
        } catch (Exception e)  {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public void StatusChanged(EvoAPIenums.SC_Status Status) {
        currentStatus = Status;
        logger.log(Level.INFO, MessageFormat.format("Status: {0}.\n", currentStatus.name()));
    }

    public boolean PrepareProcess(String sProcessName)  {
        // This feature is only allowed in Plus Mode
        if (!evoAPISystem.IsPlusMode())
            return false;
        try {
            evoAPISystem.PrepareProcess(sProcessName);
            Thread.sleep(10000);//Wait till the process loaded, depends on complexity of the process
            logger.log(Level.INFO, "Failed inside PrepareProcess()...\n");
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean StartProcess(String sProcessName, String sProcessParam, short iPriority) throws Exception  {

        // This command is only allowed in Plus Mode.
        if (!evoAPISystem.IsPlusMode())
            return false;
        try {
            evoAPISystem.Initialize();
            if (WaitStatusChange(EvoAPIenums.SC_Status.STATUS_INITIALIZED) && evoAPISystem.PrepareProcess(sProcessName)) {
                //Start the process
                int iProcessID = evoAPISystem.StartProcess(sProcessName, sProcessParam, iPriority, EvoAPIenums.SC_EmergencyLevel.PP_NORMAL);
                if (WaitProcessStatus(iProcessID)) {
                    logger.log(Level.INFO, "Successfully executed StartProcess()...\n");
                    return true;
                }
                else {
                    logger.log(Level.INFO, "Failed inside StartProcess()...\n");
                    return false;
                }
            }
            else
                return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean PrepareScript(String sScriptName) {

        // This command is only allowed in Standard Mode.
        if (!evoAPISystem.IsStandardMode())
            return false;
        try {
            int iScriptID = evoAPISystem.PrepareScript(sScriptName);
            if (iScriptID != -1)  {
                int iNumberOfRacks = evoAPISystem.GetNumberOfRacks(iScriptID);
                String sRackName = "", sRackLabel = "", sRackCarrierName = "";
                int iRackLocation = 0, iRackGrid = 0, iRackSite = 0;
                for(int iRackCount = 0; iRackCount<iNumberOfRacks; iRackCount++)
                {
                    if (evoAPISystem.GetRack(iScriptID, iRackCount, sRackName, sRackLabel, iRackLocation, iRackGrid, iRackSite, sRackCarrierName)) {
                        String sMessage = MessageFormat.format("There are: {0} racks used in this script.\nIndex: {1}\n.RackName: {2}.\nRackLabel: {3}.",
                                iNumberOfRacks, iRackCount, sRackName, sRackLabel);
                        logger.log(Level.INFO, sMessage);
                    }
                }
                Thread.sleep(5000);
                if (WaitScriptStatus(iScriptID)) {
                    logger.log(Level.INFO, "Successfully executed PrepareScript()...\n");
                    return true;
                }
                else {
                    logger.log(Level.INFO, "Failed inside PrepareScript()...\n");
                    return false;
                }
            }
            else
                return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean StartScript(String sScriptName) {
        // "This command is only allowed in Standard Mode."
        // This command is only allowed in Standard Mode.
        if (!evoAPISystem.IsStandardMode())
            return false;
        try {
            int iScriptID = evoAPISystem.PrepareScript(sScriptName);
            if (iScriptID != -1) {
                evoAPISystem.Initialize();
                if (WaitStatusChange(EvoAPIenums.SC_Status.STATUS_INITIALIZED)) {
                    Thread.sleep(5000);
                    short lineNum = 0;
                    evoAPISystem.StartScript(iScriptID, lineNum, lineNum);
                    if (WaitScriptStatus(iScriptID))   {
                        logger.log(Level.INFO, "Successfully executed StartScript()...\n");
                        return true;
                    }
                    else {
                        logger.log(Level.INFO, "Failed inside StartScript()...\n");
                        return false;
                    }
                }
                else
                    return false;
            }
            else
                return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean StartEVOware() {
        try {
            //Log on, this takes a few seconds has to wait til status = STATUS_NOTINITIALIZED or STATUS_INITIALIZED
            evoAPISystem.Logon();
            StatusChanged(evoAPISystem.GetStatus());  // Init status, since system might be already running.
            WaitStatusChange(EvoAPIenums.SC_Status.STATUS_LOADING);
            Thread.sleep(2000); //allow some extra time till evoware is completly loaded

            Config config = new Config();
            if (config.getMode() == 0)
                logger.log(Level.INFO, "Successfully executed StartStandard().\n");
            else
                logger.log(Level.INFO, "Successfully executed StartPlus().\n");

            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

}
