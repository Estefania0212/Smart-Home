/*LightOffCommand.java*/
package com.estef.smarthome.commands;

import com.estef.smarthome.SerialManager;

public class LightOffCommand implements Command {
    private final SerialManager serial;

    public LightOffCommand(SerialManager s) {
        this.serial = s;
    }

    @Override
    public void execute() {
        serial.send("LIGHT:OFF");
    }

    @Override
    public String serialize() {
        return "LIGHT:OFF";
    }
}
