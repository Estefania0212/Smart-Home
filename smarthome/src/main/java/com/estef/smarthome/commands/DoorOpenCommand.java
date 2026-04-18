
/*DoorOpenCommand.java*/
package com.estef.smarthome.commands;

import com.estef.smarthome.SerialManager;

public class DoorOpenCommand implements Command {
    private final SerialManager serial;

    public DoorOpenCommand(SerialManager s) {
        this.serial = s;
    }

    @Override
    public void execute() {
        serial.send("DOOR:OPEN");
    }

    @Override
    public String serialize() {
        return "DOOR:OPEN";
    }
}
