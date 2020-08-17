package com.shubham.wowoviewpager.Enum;

import android.animation.TimeInterpolator;
import android.graphics.PointF;

import java.util.ArrayList;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;


public class Ease implements TimeInterpolator {

    private static final int EaseTypesNumber = 31;

    public static final int InSine = 0;
    public static final int OutSine = 1;
    public static final int InOutSine = 2;
    public static final int InQuad = 3;
    public static final int OutQuad = 4;
    public static final int InOutQuad = 5;
    public static final int InCubic = 6;
    public static final int OutCubic = 7;
    public static final int InOutCubic = 8;
    public static final int InQuart = 9;
    public static final int OutQuart = 10;
    public static final int InOutQuart = 11;
    public static final int InQuint = 12;
    public static final int OutQuint = 13;
    public static final int InOutQuint= 14;
    public static final int InExpo = 15;
    public static final int OutExpo = 16;
    public static final int InOutExpo = 17;
    public static final int InCirc = 18;
    public static final int OutCirc = 19;
    public static final int InOutCirc = 20;
    public static final int InBack = 21;
    public static final int OutBack = 22;
    public static final int InOutBack = 23;
    public static final int InElastic = 24;
    public static final int OutElastic = 25;
    public static final int InOutElastic = 26;
    public static final int InBounce = 27;
    public static final int OutBounce = 28;
    public static final int InOutBounce = 29;
    public static final int Linear = 30;

    private int ease;
    private PointF a = new PointF();
    private PointF b = new PointF();
    private PointF c = new PointF();
    private Boolean ableToDefineWithControlPoints = true;

    private static ArrayList<Ease> eases;

    public static Ease getInstance(int ease) {
        if (eases == null) {
            eases = new ArrayList<>(EaseTypesNumber);
            for (int length = EaseTypesNumber - 1; length >= 0; length--) eases.add(null);
        }
        Ease easer = eases.get(ease);
        if (easer == null) {
            easer = new Ease(ease);
            eases.set(ease, easer);
        }
        return easer;
    }

    private Ease(int ease) {
        switch (ease) {
            case InBack:
                init(0.6, -0.2, 0.735, 0.045);
                break;
            case InCirc:
                init(0.6, 0.04, 0.98, 0.335);
                break;
            case InCubic:
                init(0.55, 0.055, 0.675, 0.19);
                break;
            case InExpo:
                init(0.95, 0.05, 0.795, 0.035);
                break;
            case InSine:
                init(0.47, 0, 0.745, 0.715);
                break;
            case InQuad:
                init(0.55, 0.085, 0.68, 0.53);
                break;
            case InQuint:
                init(0.755, 0.05, 0.855, 0.06);
                break;
            case InQuart:
                init(0.895, 0.03, 0.685, 0.22);
                break;
            case OutBack:
                init(0.174, 0.885, 0.32, 1.275);
                break;
            case OutCirc:
                init(0.075, 0.82, 0.165, 1);
                break;
            case OutCubic:
                init(0.215, 0.610, 0.355, 1);
                break;
            case OutExpo:
                init(0.19, 1, 0.22, 1);
                break;
            case OutSine:
                init(0.39, 0.575, 0.565, 1);
                break;
            case OutQuad:
                init(0.25, 0.46, 0.45, 0.94);
                break;
            case OutQuint:
                init(0.23, 1, 0.32, 1);
                break;
            case OutQuart:
                init(0.165, 0.84, 0.44, 1);
                break;
            case InOutBack:
                init(0.68, -0.55, 0.265, 1.55);
                break;
            case InOutCirc:
                init(0.785, 0.135, 0.15, 0.86);
                break;
            case InOutCubic:
                init(0.645, 0.045, 0.335, 1);
                break;
            case InOutExpo:
                init(1, 0, 0, 1);
                break;
            case InOutSine:
                init(0.445, 0.05, 0.55, 0.95);
                break;
            case InOutQuad:
                init(0.455, 0.03, 0.515, 0.955);
                break;
            case InOutQuint:
                init(0.86, 0, 0.07, 1);
                break;
            case InOutQuart:
                init(0.77, 0, 0.175, 1);
                break;
            case Linear:
                init(0, 0, 1, 1);
                break;
            case InBounce:
            case OutBounce:
            case InOutBounce:
            case InElastic:
            case OutElastic:
            case InOutElastic:
                ableToDefineWithControlPoints = false;
                break;
            default:
                throw new RuntimeException("Ease not found!");
        }
        this.ease = ease;
    }

    @Override
    public float getInterpolation(float offset) {
        if (ableToDefineWithControlPoints) {
            return getBezierCoordinateY(getXForTime(offset));
        } else {
            switch (ease) {
                case InBounce:
                    return getEaseInBounceOffset(offset);
                case InElastic:
                    return getEaseInElasticOffset(offset);
                case OutBounce:
                    return getEaseOutBounceOffset(offset);
                case OutElastic:
                    return getEaseOutElasticOffset(offset);
                case InOutBounce:
                    return getEaseInOutBounceOffset(offset);
                case InOutElastic:
                    return getEaseInOutElasticOffset(offset);
                default:
                    throw new RuntimeException("Wrong ease-enum initialize method.");
            }
        }
    }

    private void init(float startX, float startY, float endX, float endY) {
        PointF start = new PointF(startX, startY);
        PointF end = new PointF(endX, endY);
        c.x = 3 * start.x;
        b.x = 3 * (end.x - start.x) - c.x;
        a.x = 1 - c.x - b.x;
        c.y = 3 * start.y;
        b.y = 3 * (end.y - start.y) - c.y;
        a.y = 1 - c.y - b.y;
    }

    private void init(double startX, double startY, double endX, double endY) {
        init((float) startX, (float) startY, (float) endX, (float) endY);
    }

    private float getXForTime(float time) {
        float x = time;
        float z;
        for (int i = 1; i < 14; i++) {
            z = getBezierCoordinateX(x) - time;
            if (Math.abs(z) < 1e-3) {
                break;
            }
            x -= z / getX(x);
        }
        return x;
    }

    private float getBezierCoordinateY(float time) {
        return time * (c.y + time * (b.y + time * a.y));
    }

    private float getBezierCoordinateX(float time) {
        return time * (c.x + time * (b.x + time * a.x));
    }

    private float getX(float t) {
        return c.x + t * (2 * b.x + 3 * a.x * t);
    }

    private float getEaseInBounceOffset(float offset) {
        float b = 0;
        float c = 1;
        float d = 1;
        return c - getEaseBounceOffset2(d - offset, 0, c, d) + b;
    }

    private float getEaseOutBounceOffset(float offset) {
        float b = 0;
        float c = 1;
        float d = 1;
        if ((offset /= d) < (1 / 2.75))
        {
            return c * (7.5625f * offset * offset) + b;
        }
        else if (offset < (2 / 2.75))
        {
            offset -= (1.5 / 2.75);
            return c * (7.5625f * offset * offset + 0.75f) + b;
        }
        else if (offset < (2.5 / 2.75))
        {
            offset -= (2.25 / 2.75);
            return c * (7.5625f * offset * offset + 0.9375f) + b;
        }
        else
        {
            offset -= 2.625 / 2.75;
            return c * (7.5625f * offset * offset + 0.984375f) + b;
        }
    }

    private float getEaseInOutBounceOffset(float offset) {
        float b = 0;
        float c = 1;
        float d = 1;
        if (offset < d / 2) {
            return getEaseBounceOffset1(offset * 2, 0, c, d) * 0.5f + b;
        } else {
            return getEaseBounceOffset2(offset * 2, 0, c, d) * 0.5f + c * 0.5f + b;
        }
    }

    private float getEaseBounceOffset1(float t, float b, float c, float d) {
        return c - getEaseBounceOffset2(d - t, 0, c, d) + b;
    }

    private float getEaseBounceOffset2(float t, float b, float c, float d) {
        if ((t /= d) < (1 / 2.75)) {
            return c * (7.5625f * t * t) + b;
        } else if (t < (2 / 2.75)) {
            t -= 1.5 / 2.75;
            return c * (7.5625f * t * t + 0.75f) + b;
        } else if (t < (2 / 2.75)) {
            t -= 1.5 / 2.75;
            return c * (7.5625f * t * t + 0.9375f) + b;
        } else {
            t -= 2.625 / 2.75;
            return c * (7.5625f * t * t + 0.984375f) + b;
        }
    }

    private float getEaseInElasticOffset(float offset) {
        float b = 0;
        float c = 1;
        float d = 1;
        if (offset == 0) {
            return b;
        }
        if ((offset /= d) == 1) {
            return b + c;
        }
        float p = d * 0.3f;
        float s = p / 4;
        offset -= 1;
        return - (c * (float)pow(2, 10 * offset)
                * (float)sin((offset * d - s) * (2 * (float)PI) / p)) + b;
    }

    private float getEaseOutElasticOffset(float offset) {
        float b = 0;
        float c = 1;
        float d = 1;
        if (offset == 0) {
            return b;
        }
        if ((offset /= d) == 1) {
            return b + c;
        }
        float p = d * 0.3f;
        float s = p / 4;
        return (c * (float)pow(2, -10 * offset)
                * (float)sin((offset * d - s) * (2 * (float)PI) / p) + c + b);
    }

    private float getEaseInOutElasticOffset(float offset) {
        float b = 0;
        float c = 1;
        float d = 1;
        if (offset == 0) {
            return b;
        }
        if ((offset /= d / 2) == 2) {
            return b + c;
        }
        float p = d * 0.45f;
        float s = p / 4;
        if (offset < 1) {
            offset -= 1;
            return -0.5f * (c * (float)pow(2, 10 * offset)
                    * (float)sin((offset * d - s) * (2 * (float)PI) / p)) + b;
        } else {
            offset -= 1;
            return c * (float)pow(2, -10 * offset)
                    * (float)sin((offset * d - s) * (2 * (float)PI) / p) * 0.5f + c + b;
        }
    }
}
