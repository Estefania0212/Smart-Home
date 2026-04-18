/*DoorClosed.java*/
package com.estef.smarthome.state;

public class DoorClosed implements DoorState {
    @Override
    public DoorState toggle() {
        return new DoorOpen();
    }

    @Override
    public String name() {
        return "CERRADA";
    }
}
