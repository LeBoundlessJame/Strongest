package com.boundless.util;

import com.boundless.client.CameraShake;

public interface CameraShakeAccessor {
    default void boundless$addCameraShake(CameraShake cameraShake) {}
}
