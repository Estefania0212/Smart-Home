/*LightOnCommand.java*/
package com.estef.smarthome.commands;

import com.estef.smarthome.SerialManager;

public class LightOnCommand implements Command {
    private final SerialManager serial;

    public LightOnCommand(SerialManager s) {
        this.serial = s;
    }

    @Override
    public void execute() {
        serial.send("LIGHT:ON");
    }

    @Override
    public String serialize() {
        return "LIGHT:ON";
    }
}
