package dev.scappi.opticam.util;

public final class MathUtil {

    private MathUtil() {}

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float wrapDegrees(float degrees) {
        degrees %= 360f;
        if (degrees >= 180f) degrees -= 360f;
        if (degrees < -180f) degrees += 360f;
        return degrees;
    }

    public static float approach(float current, float target, float maxStep) {
        float diff = wrapDegrees(target - current);

        if (Math.abs(diff) <= maxStep) return target;

        return current + Math.signum(diff) * maxStep;
    }

    public static double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }
}