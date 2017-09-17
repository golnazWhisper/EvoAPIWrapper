package com.robotwhisperer.app.EvoAPI;

/**
 * Created by admin_golnaz on 9/16/2017.
 */
public class EvoAPIenums {

    public enum SC_Status {
        STATUS_UNKNOWN (0),
        STATUS_NOINTRUMENTS (1),
        STATUS_LOADING (2),
        STATUS_NOTINITIALIZED (4),
        STATUS_INITIALIZING (8),
        STATUS_INITIALIZED (16),
        STATUS_SHUTTINGDOWN (32),
        STATUS_SHUTDOWN (64),
        STATUS_UNLOADING (128),
        STATUS_RUNNING (256),
        STATUS_PAUSEREQUESTED (512),
        STATUS_PAUSED (1024),
        STATUS_RESOURCEMISSING (2048),
        STATUS_DEADLOCK (4096),
        STATUS_EXECUTIONERROR (8192),
        STATUS_TIMEVIOLATION (16384),
        STATUS_IDLE (65536),
        STATUS_BUSY (131072),
        STATUS_ABORTED (262144),
        STATUS_STOPPED (524288),
        STATUS_PIPETTING (1048576),
        STATUS_ERROR (2097152),
        STATUS_SIMULATION (4194304),
        STATUS_LOGON_ERROR (8388608),
        STATUS_CONNECTION_ERROR (16777216);

        private int id;
        SC_Status(int id) { this.id = id; }
        public int value() { return id; }

        private static SC_Status[] values = null;
        public static SC_Status parseInt(int i) {
            if(SC_Status.values == null) {
                SC_Status.values = SC_Status.values();
            }
            for (int j=0; j<values.length; j++)
                if (SC_Status.values[j].value() == i)
                    return SC_Status.values[j];
            return SC_Status.STATUS_UNKNOWN;
        }
    }

    public enum SC_ProcessStatus  {
        PS_IDLE (0),
        PS_BUSY (1),
        PS_FINISHED (2),
        PS_ERROR (3),
        PS_STOPPED (4);

        private int id;
        SC_ProcessStatus(int id) { this.id = id; }
        public int value() { return id; }

        private static SC_ProcessStatus[] values = null;
        public static SC_ProcessStatus parseInt(int i) {
            if(SC_ProcessStatus.values == null) {
                SC_ProcessStatus.values = SC_ProcessStatus.values();
            }
            return SC_ProcessStatus.values[i];
        }
    }

    public enum SC_EmergencyLevel  {
        PP_NORMAL (0),
        PP_EMERGENCY (1),
        PP_HIGHEMERGENCY (2);

        private int id;
        SC_EmergencyLevel(int id) { this.id = id; }
        public int value() { return id; }

        private static SC_EmergencyLevel[] values = null;
        public static SC_EmergencyLevel parseInt(int i) {
            if(SC_EmergencyLevel.values == null) {
                SC_EmergencyLevel.values = SC_EmergencyLevel.values();
            }
            return SC_EmergencyLevel.values[i];
        }
    }

    public enum SC_ScriptStatus  {
        SS_UNKNOWN (0),
        SS_IDLE (1),
        SS_BUSY (2),
        SS_ABORTED (3),
        SS_STOPPED (4),
        SS_PIPETTING (5),
        SS_PAUSED (6),
        SS_ERROR (7),
        SS_SIMULATION (8),
        SS_STATUS_ERROR (9);

        private int id;
        SC_ScriptStatus(int id) { this.id = id; }
        public int value() { return id; }

        private static SC_ScriptStatus[] values = null;
        public static SC_ScriptStatus parseInt(int i) {
            if(SC_ScriptStatus.values == null) {
                SC_ScriptStatus.values = SC_ScriptStatus.values();
            }
            return SC_ScriptStatus.values[i];
        }
    }

    public enum SC_VariableScope  {
        VS_PROCESS (0),
        VS_ITERATION (1);

        private int id;
        SC_VariableScope(int id) { this.id = id; }
        public int value() { return id; }

        private static SC_VariableScope[] values = null;
        public static SC_VariableScope parseInt(int i) {
            if(SC_VariableScope.values == null) {
                SC_VariableScope.values = SC_VariableScope.values();
            }
            return SC_VariableScope.values[i];
        }
    }

    public enum SC_LampStatus {
        LAMP_OFF (0),
        LAMP_GREEN (1),
        LAMP_GREENFLASHING (2),
        LAMP_REDFLASHING (3);

        private int id;
        SC_LampStatus(int id) { this.id = id; }
        public int value() { return id; }

        private static SC_LampStatus[] values = null;
        public static SC_LampStatus parseInt(int i) {
            if(SC_LampStatus.values == null) {
                SC_LampStatus.values = SC_LampStatus.values();
            }
            return SC_LampStatus.values[i];
        }
    }

    public enum SC_TipType {
        TIP_STANDARD (0),
        TIP_DITI (1),
        TIP_FIXLOVOL (2),
        TIP_DITILOVOL (3),
        TIP_ACTIVE (4),
        TIP_TEMOFIXED (5),
        TIP_TEMO96DITI (6),
        TIP_TEMO384IMPULSE (7),
        TIP_TEMO384 (8);

        private int id;
        SC_TipType(int id) { this.id = id; }
        public int value() { return id; }

        private static SC_TipType[] values = null;
        public static SC_TipType parseInt(int i) {
            if(SC_TipType.values == null) {
                SC_TipType.values = SC_TipType.values();
            }
            return SC_TipType.values[i];
        }
    }
    
}
