package com.boundless.util.interfaces;

import com.boundless.client.CameraShake;

public interface CameraShakeAccessor {
    default void boundless$addCameraShake(CameraShake cameraShake) {}
}
