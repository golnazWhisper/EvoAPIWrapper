package com.robotwhisperer.app.EvoAPI;

import com.javonet.Javonet;
import com.javonet.JavonetFramework;
import com.javonet.api.NObject;
import org.apache.commons.lang.math.NumberUtils;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by admin_golnaz on 9/16/2017.
 */
public class EvoAPISystem {

    private static Logger logger;
    private NObject evoSystem;
    private enum ProductVersion {
        Standard, Plus
    }
    private ProductVersion productVersion;

    public EvoAPISystem() throws Exception {
        logger = Logger.getLogger(EvoAPISystem.class.getName());
        Javonet.activate("golnaz@robotwhispererllc.com", "s8DX-o2E3-c3KR-Yd78-p2CH", JavonetFramework.v45);
        Javonet.addReference("Interop.Evoapi.dll");
        evoSystem = Javonet.New("SystemClass");
    }

    private void SetProductVersion(int iMode) {
        if (iMode == 0)
            productVersion = ProductVersion.Standard;
        else
            productVersion = ProductVersion.Plus;
    }

    public boolean IsPlusMode()  {
        return productVersion == ProductVersion.Plus;
    }

    public boolean IsStandardMode()  {
        return productVersion == ProductVersion.Standard;
    }

    private boolean isPlusMode(String functionName)  {
        if (productVersion == ProductVersion.Standard) {
            logger.log(Level.WARNING, "Attempted to invoke " + functionName + " in Standard Mode. This feature is only allowed in Plus Mode.\n");
            return false;
        }
        return true;
    }

    private boolean isStandardMode(String functionName)  {
        if (productVersion == ProductVersion.Plus) {
            logger.log(Level.WARNING, "Attempted to invoke " + functionName + " in Standard Mode. This feature is only allowed in Standard Mode.\n");
            return false;
        }
        return true;
    }

    public void Logon() throws Exception  {

        Config config = new Config();
        try {
            SetProductVersion(config.getMode());
            evoSystem.invoke("Logon", config.getUserName(), config.getPassword(),
                                                 config.getMode(), config.getSimulation()?1:0);
            String logMessage = MessageFormat.format("Invoked Logon with userName {0}, mode {1}, and simulation {2}.\n",
                    config.getUserName(), productVersion.name(), config.getSimulation()?"on":"off");
            logger.log(Level.INFO, logMessage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void Logoff() throws Exception  {
        try {
            evoSystem.invoke("Logoff");
            logger.log(Level.INFO, "Invoked Logoff");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public boolean PrepareProcess(String sProcessName) throws Exception  {
        if (isStandardMode("PrepareProcess"))
            return false;

        try {
            evoSystem.invoke("PrepareProcess", sProcessName);
            Thread.sleep(10000);//Wait till the process loaded, depends on complexity of the process
            logger.log(Level.INFO, "Successfully executed PrepareProcess()...\n");
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public int StartProcess(String sProcessName, String sProcessParam, short iPriority, EvoAPIenums.SC_EmergencyLevel emergencyLevel)  {
        if (isStandardMode("StartProcess"))
            return -1;

        try {
            int iProcessID = evoSystem.invoke("StartProcess", sProcessName, sProcessParam, iPriority, emergencyLevel.value());
            logger.log(Level.INFO, "Invoked StartProcess");
            return iProcessID;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return -1;
        }
    }

    public int PrepareScript(String sScriptName) {
        if (isPlusMode("PrepareScript"))
            return -1;

        try {
            int iScriptID = evoSystem.invoke("PrepareScript", sScriptName);
            logger.log(Level.INFO, "Invoked PrepareScript");
            return iScriptID;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return -1;
        }
    }

    public void StartScript(int iScriptID, short iStartLine, short iEndLine) {
        if (isPlusMode("StartScript"))
            return;

        try {
            evoSystem.invoke("StartScript", iScriptID, iStartLine, iEndLine);
            logger.log(Level.INFO, "Invoked StartScript");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void Pause() throws Exception  {
        try {
            evoSystem.invoke("Pause");
            logger.log(Level.INFO, "Invoked Pause");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void Resume() throws Exception  {
        try {
            evoSystem.invoke("Resume");
            logger.log(Level.INFO, "Invoked Resume");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void Stop() throws Exception  {
        try {
            evoSystem.invoke("Stop");
            logger.log(Level.INFO, "Invoked Stop");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void Initialize() throws Exception  {
        try {
            evoSystem.invoke("Initialize");
            logger.log(Level.INFO, "Invoked Initialize");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void Shutdown() throws Exception  {
        try {
            evoSystem.invoke("Shutdown");
            logger.log(Level.INFO, "Invoked Shutdown");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void CancelProcess(int iProcessID) {
        if (isStandardMode("CancelProcess"))
            return;

        try {
            evoSystem.invoke("CancelProcess", iProcessID);
            logger.log(Level.INFO, "Invoked CancelProcess");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public int GetDeviceCount()  {
        try {
            int iCount = evoSystem.invoke("GetDeviceCount");
            logger.log(Level.INFO, "Invoked GetDeviceCount");
            return iCount;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return -1;
        }
    }

    // I think we need to create a Device wrapper first before implementing this
//    public EVOAPILib.Device GetDevice(Object)

    public EvoAPIenums.SC_Status GetStatus()  {
        try {
            com.javonet.api.NEnum javonetEnum = evoSystem.invoke("GetStatus");
            logger.log(Level.INFO, "Invoked GetStatus");
            // get the name of the enum, if name is numeric it is junk, ignore it
            // if we have not waited long enough before getting the status, this happens
            boolean isNumeric = NumberUtils.isNumber(javonetEnum.getValueName());
            if (isNumeric) {
                logger.log(Level.WARNING, "No Status value received!");
                return EvoAPIenums.SC_Status.STATUS_UNKNOWN;
            }
            else  {
                EvoAPIenums.SC_Status newStatus = EvoAPIenums.SC_Status.parseInt(javonetEnum.getValue());
                logger.log(Level.INFO, MessageFormat.format("Status: {0}.\n", newStatus.name()));
                return newStatus;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return EvoAPIenums.SC_Status.STATUS_UNKNOWN;
        }
    }

    public EvoAPIenums.SC_ScriptStatus GetScriptStatus(int iScriptID)    {
        if (isPlusMode("GetScriptStatus"))
            return null;

        try {
            com.javonet.api.NEnum javonetEnum = evoSystem.invoke("GetScriptStatus", iScriptID);
            EvoAPIenums.SC_ScriptStatus status = EvoAPIenums.SC_ScriptStatus.parseInt(javonetEnum.getValue());
            logger.log(Level.INFO, "Invoked GetScriptStatus");
            logger.log(Level.INFO, MessageFormat.format("Script Status: {0}.\n", status.name()));
            return status;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    EvoAPIenums.SC_ScriptStatus GetScriptStatusEx(int iScriptID) {
        if (isPlusMode("GetScriptStatusEx"))
            return null;

        try {
            com.javonet.api.NEnum javonetEnum = evoSystem.invoke("GetScriptStatusEx", iScriptID);
            EvoAPIenums.SC_ScriptStatus status = EvoAPIenums.SC_ScriptStatus.parseInt(javonetEnum.getValue());
            logger.log(Level.INFO, "Invoked GetScriptStatusEx");
            return status;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public EvoAPIenums.SC_ProcessStatus GetProcessStatus(int iProcessID)  {
        if (isStandardMode("GetProcessStatus"))
            return null;

        try {
            com.javonet.api.NEnum javonetEnum = evoSystem.invoke("GetProcessStatus", iProcessID);
            EvoAPIenums.SC_ProcessStatus status = EvoAPIenums.SC_ProcessStatus.parseInt(javonetEnum.getValue());
            logger.log(Level.INFO, "Invoked GetProcessStatus");
            logger.log(Level.INFO, MessageFormat.format("Process Status: {0}.\n", status.name()));
            return status;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    EvoAPIenums.SC_ProcessStatus GetProcessStatusEx(int iProcessID) {
        if (isStandardMode("GetProcessStatus"))
            return null;

        try {
            com.javonet.api.NEnum javonetEnum = evoSystem.invoke("GetProcessStatusEx", iProcessID);
            EvoAPIenums.SC_ProcessStatus status = EvoAPIenums.SC_ProcessStatus.parseInt(javonetEnum.getValue());
            logger.log(Level.INFO, "Invoked GetProcessStatusEx");
            return status;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public void SetRemoteMode(boolean bEnable)  {
        try {
            evoSystem.invoke("SetRemoteMode", bEnable?1:0);
            logger.log(Level.INFO, "Invoked SetRemoteMode");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void SetLamp(EvoAPIenums.SC_LampStatus status) {
        try {
            evoSystem.invoke("SetLamp", status.value());
            logger.log(Level.INFO, "Invoked SetLamp");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void SetDoorLocks(boolean bClose) {
        try {
            evoSystem.invoke("SetDoorLocks", bClose?1:0);
            logger.log(Level.INFO, "Invoked SetDoorLocks");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public Object GetProcessVariable(int iProcessID, String sVariableName, EvoAPIenums.SC_VariableScope variableScope) {
        if (isStandardMode("GetProcessVariable"))
            return null;

        try {
            Object valueStored = evoSystem.invoke("GetProcessVariable", iProcessID, sVariableName, variableScope);
            logger.log(Level.INFO, "Invoked GetProcessVariable");
            return valueStored;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public void SetProcessVariable(int iProcessID, String sVariableName, EvoAPIenums.SC_VariableScope variableScope, Object valueToStore) {
        if (isStandardMode("SetProcessVariable"))
            return;

        try {
            evoSystem.invoke("SetProcessVariable", iProcessID, sVariableName, variableScope.value(), valueToStore);
            logger.log(Level.INFO, "Invoked SetProcessVariable");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    Object GetScriptVariable(int iScriptID, String sVariableName) {
        try {
            Object valueStored = evoSystem.invoke("GetScriptVariable", iScriptID, sVariableName);
            logger.log(Level.INFO, "Invoked GetScriptVariable");
            return valueStored;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public void SetScriptVariable(int iScriptID, String sVariableName, Object valueToStore) {
        try {
            evoSystem.invoke("SetScriptVariable", iScriptID, sVariableName, valueToStore);
            logger.log(Level.INFO, "Invoked SetScriptVariable");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public int GetNumberOfRacks(int iScriptID)  {
        try {
            int iNumberOfRacks = evoSystem.invoke("GetNumberOfRacks", iScriptID);
            logger.log(Level.INFO, "Invoked GetNumberOfRacks");
            return iNumberOfRacks;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return -1;
        }
    }

    public boolean GetRack(int iScriptID, int iRackCount, String sRackName, String sRackLabel,
                           int iRackLocation, int iRackGrid, int iRackSite, String sRackCarrierName)   {
        try {
            int iNumberOfRacks = evoSystem.invoke("GetRack", iScriptID, iRackCount, sRackName, sRackLabel,
                    iRackLocation, iRackGrid, iRackSite, sRackCarrierName);
            logger.log(Level.INFO, "Invoked GetRack");
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public void SetRack(int iScriptID, int iIndex, int iRackLocation, String sBarcode) {
        try {
            evoSystem.invoke("SetRack", iScriptID, iIndex, iRackLocation, sBarcode);
            logger.log(Level.INFO, "Invoked SetRack");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public int GetLCCount()  {
        try {
            int iCount = evoSystem.invoke("GetLCCount");
            logger.log(Level.INFO, "Invoked GetLCCount");
            return iCount;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return -1;
        }
    }

    public void GetLCInfo(int iLC, String sLC, Boolean bDefault, Boolean bCustom)  {
        try {
            evoSystem.invoke("GetLCInfo", iLC, sLC, bDefault, bCustom);
            logger.log(Level.INFO, "Invoked GetLCInfo");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public int GetSubLCCount(int iIndex)  {
        try {
            int iCount = evoSystem.invoke("GetSubLCCount", iIndex);
            logger.log(Level.INFO, "Invoked GetSubLCCount");
            return iCount;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return -1;
        }
    }

    public void GetSubLCInfo(int iLC, int iSubLC, EvoAPIenums.SC_TipType tipType, Boolean bAllVol, Double dMinVol, Double dMaxVol)  {
        try {
            evoSystem.invoke("GetSubLCInfo", iLC, iSubLC, tipType.value(), bAllVol, dMinVol, dMaxVol);
            logger.log(Level.INFO, "Invoked GetSubLCInfo");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void ReadLiquidClasses()  {
        try {
            evoSystem.invoke("ReadLiquidClasses");
            logger.log(Level.INFO, "Invoked ReadLiquidClasses");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void GetWindowHandles(int mainHwnd, int scriptHwnd, int wktbHwnd, int logHwnd)  {
        try {
            evoSystem.invoke("GetWindowHandles", mainHwnd, scriptHwnd, wktbHwnd, logHwnd);
            logger.log(Level.INFO, "Invoked GetWindowHandles");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void HideGUI(boolean bHide)  {
        try {
            evoSystem.invoke("HideGUI", bHide?1:0);
            logger.log(Level.INFO, "Invoked HideGUI");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void ExecuteScriptCommand(String sCommand) {
        try {
            evoSystem.invoke("ExecuteScriptCommand", sCommand);
            logger.log(Level.INFO, "Invoked ExecuteScriptCommand");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void StartADH()  {
        if (isStandardMode("StartADH"))
            return;

        try {
            evoSystem.invoke("StartADH");
            logger.log(Level.INFO, "Invoked StartADH");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void ResetStoredADHInfo()  {
        if (isStandardMode("ResetStoredADHInfo"))
            return;

        try {
            evoSystem.invoke("ResetStoredADHInfo");
            logger.log(Level.INFO, "Invoked ResetStoredADHInfo");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void StartADHLoadProcess(EvoAPIenums.SC_EmergencyLevel emergencyLevel) {
        if (isStandardMode("StartADHLoadProcess"))
            return;

        try {
            evoSystem.invoke("StartADHLoadProcess", emergencyLevel.value());
            logger.log(Level.INFO, "Invoked StartADHLoadProcess");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void StartADHUnloadProcess()  {
        if (isStandardMode("StartADHUnloadProcess"))
            return;

        try {
            evoSystem.invoke("StartADHUnloadProcess");
            logger.log(Level.INFO, "Invoked StartADHUnloadProcess");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void GetSystemInfo(Boolean bRunningPlusMode, Boolean bRunningSimulationMode, String sSoftwareVerNum, String sInstSerialNum)  {
        try {
            evoSystem.invoke("GetSystemInfo", bRunningPlusMode, bRunningSimulationMode, sSoftwareVerNum, sInstSerialNum);
            logger.log(Level.INFO, "Invoked GetSystemInfo");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

}

