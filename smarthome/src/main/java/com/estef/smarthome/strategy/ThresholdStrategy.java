/*ThresholdStrategy.java*/
package com.estef.smarthome.strategy;

public interface ThresholdStrategy {
    boolean shouldWarn(long millisSinceOn);
}
