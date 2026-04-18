/*CommandFactory.java*/
package com.estef.smarthome.factory;

import com.estef.smarthome.SerialManager;
import com.estef.smarthome.commands.*;

public class CommandFactory {
    public static Command create(String type, SerialManager s) {
        switch (type) {
            case "LIGHT_ON":
                return new LightOnCommand(s);
            case "LIGHT_OFF":
                return new LightOffCommand(s);
            case "DOOR_OPEN":
                return new DoorOpenCommand(s);
            case "DOOR_CLOSE":
                return new DoorCloseCommand(s);
            default:
                return null;
        }
    }
}
