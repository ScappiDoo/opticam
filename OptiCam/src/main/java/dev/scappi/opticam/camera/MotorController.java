package dev.scappi.opticam.camera;

import org.bukkit.entity.Player;

public final class MotorController {

    private static final float MAX_YAW = 90f;
    private static final float MAX_PITCH = 30f;
    private static final float MOTOR_SPEED = 3.0f;

    public static void update(CameraSession session, float targetYaw, float targetPitch) {

        Player player = session.getPlayer();
        float baseYaw = session.getCamera().getBaseYaw();

        // Calculate relative yaw difference
        float relative = wrap(targetYaw - baseYaw);

        // Clamp to ±90°
        relative = clamp(relative, -MAX_YAW, MAX_YAW);

        float desiredYaw = baseYaw + relative;

        float currentYaw = session.getCurrentYaw();
        float newYaw = approach(currentYaw, desiredYaw, MOTOR_SPEED);

        float newPitch = clamp(targetPitch, -MAX_PITCH, MAX_PITCH);

        session.setCurrentYaw(newYaw);
        session.setCurrentPitch(newPitch);

        // IMPORTANT: only rotate, do NOT teleport
        player.setRotation(targetYaw, targetPitch);
    }

    private static float approach(float current, float target, float speed) {
        float diff = wrap(target - current);
        if (Math.abs(diff) <= speed) return target;
        return current + Math.signum(diff) * speed;
    }

    private static float wrap(float angle) {
        angle %= 360f;
        if (angle > 180f) angle -= 360f;
        if (angle < -180f) angle += 360f;
        return angle;
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}