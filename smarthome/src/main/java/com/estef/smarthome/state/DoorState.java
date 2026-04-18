/*DoorState.java*/
package com.estef.smarthome.state;

public interface DoorState {
    DoorState toggle(); // devuelve nuevo estado

    String name();
}
