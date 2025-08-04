package com.boundless.util;

public interface FlightAccess {
    float boundless$getFlightRotation();
    void boundless$setFlightRotation(float rotation);
    void boundless$adjustFlightRotation(float rotationAdjustment, float min, float max);
    void boundless$returnToDefaultRotation(float returnSpeed);
}
