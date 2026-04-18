/*Command.java*/
package com.estef.smarthome.commands;

public interface Command {
    void execute();

    String serialize();
}
