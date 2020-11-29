/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.particle.shapes;

import com.dumbdogdiner.stickyapi.bukkit.particle.Orientation;
import com.dumbdogdiner.stickyapi.bukkit.particle.Parametric;
import com.dumbdogdiner.stickyapi.bukkit.particle.ParticleSystem;
import com.dumbdogdiner.stickyapi.bukkit.particle.Shape;
import org.apache.commons.lang.Validate;
import org.bukkit.Particle;

public class OutlinedSphere implements Shape {
    @Override
    public void draw(ParticleSystem system, Particle particle, Particle.DustOptions data) {

    }

    @Override
    public void drawAbsolute(ParticleSystem system, Particle particle, Particle.DustOptions data) {

    }

    public enum SphereType {
        XY,
        YZ,
        XZ,
        XYZ,
        XY_SPIRAL,
        YZ_SPIRAL,
        XZ_SPIRAL
    }

    public static final int MIN_DIVISIONS = 4;
    public static final int MAX_DIVISIONS = 32;
    public static final int MAX_RADIUS = 128;

    private Parametric parametric;

    public OutlinedSphere(double x, double y, double z, double r, int divisions, SphereType orientation) {
        if (r > MAX_RADIUS) {
            throw new IllegalArgumentException("Tried to draw sphere with absurd radius (>" + MAX_RADIUS + ")!");
        }
        if (r < MIN_DIVISIONS || r > MAX_DIVISIONS) {
            throw new IllegalArgumentException("Tried to draw sphere with invalid divisions (>"
                    + MAX_DIVISIONS + " or <" + MIN_DIVISIONS + ")!");
        }

        switch (orientation) {
            case XY:
                this.parametric = new Parametric() {
                    @Override
                    public double x(double t) {
                        return Math.cos(t) * r + x;
                    }

                    @Override
                    public double y(double t) {
                        return Math.sin(t) * r + y;
                    }

                    @Override
                    public double z(double t) {
                        return Math.floor(t / (2 * Math.PI)) - (r / 2D) + z;
                    }
                };
                break;

            case XZ:
                this.parametric = new Parametric() {
                    @Override
                    public double x(double t) {
                        return Math.cos(t) * r + x;
                    }

                    @Override
                    public double y(double t) {
                        return Math.floor(t / (2 * Math.PI)) - (r / 2D) + y;
                    }

                    @Override
                    public double z(double t) {
                        return Math.sin(t) * r + z;
                    }
                };
                break;

            case YZ:
                this.parametric = new Parametric() {
                    @Override
                    public double x(double t) {
                        return Math.floor(t / (2 * Math.PI)) - (r / 2D) + x;
                    }

                    @Override
                    public double y(double t) {
                        return Math.cos(t) * r + y;
                    }

                    @Override
                    public double z(double t) {
                        return Math.sin(t) * r + z;
                    }
                };
                break;
            default:
                throw new UnsupportedOperationException("I haven't written that yet");
        }


    }
}
