/*DoorCloseCommand.java*/
package com.estef.smarthome.commands;

import com.estef.smarthome.SerialManager;

public class DoorCloseCommand implements Command {
    private final SerialManager serial;

    public DoorCloseCommand(SerialManager s) {
        this.serial = s;
    }

    @Override
    public void execute() {
        serial.send("DOOR:CLOSE");
    }

    @Override
    public String serialize() {
        return "DOOR:CLOSE";
    }
}
