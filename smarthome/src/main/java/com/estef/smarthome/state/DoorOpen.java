/*DoorOpen.java*/
package com.estef.smarthome.state;

public class DoorOpen implements DoorState {
    @Override
    public DoorState toggle() {
        return new DoorClosed();
    }

    @Override
    public String name() {
        return "ABIERTA";
    }
}
