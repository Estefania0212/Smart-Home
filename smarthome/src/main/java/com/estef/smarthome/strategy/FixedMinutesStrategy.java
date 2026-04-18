/*FixedMinutesStrategy.java*/
package com.estef.smarthome.strategy;

public class FixedMinutesStrategy implements ThresholdStrategy {
    private final long limit;

    public FixedMinutesStrategy(int minutes) {
        this.limit = minutes * 60L * 1000L;
    }

    @Override
    public boolean shouldWarn(long millisSinceOn) {
        return millisSinceOn >= limit;
    }
}
