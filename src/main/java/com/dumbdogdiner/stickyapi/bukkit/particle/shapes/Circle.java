/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.particle.shapes;

import com.dumbdogdiner.stickyapi.bukkit.particle.Orientation;
import com.dumbdogdiner.stickyapi.bukkit.particle.Parametric;
import com.dumbdogdiner.stickyapi.bukkit.particle.ParticleSystem;
import com.dumbdogdiner.stickyapi.bukkit.particle.Shape;

import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

/**
 * Draws a circle.
 * 
 * @since 2.0
 */
public class Circle implements Shape {
    /**
     * Prevent developers from creating absurdly large circles - too many particles
     * will cause nearby clients to crash.
     */
    final double MAX_RADIUS = 250;

    private final double r;

    private Parametric parametric;

    /**
     * Construct a new circle with center (x,y,z) and radius r.
     *
     * @param x           X coordinate for center of the circle.
     * @param y           Y coordinate for center of the circle.
     * @param z           Z coordinate for center of the circle.
     * @param r           Size of the circle's radius.
     * @param orientation Orientation of the circle.
     */
    public Circle(double x, double y, double z, double r, Orientation orientation) {
        if (r > MAX_RADIUS) {
            throw new IllegalArgumentException("Tried to draw circle with absurd radius (>250)!");
        }

        this.r = r;

        // set the orientation of the circle
        switch (orientation) {
            case XY: {
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
                        return z;
                    }
                };
                break;
            }
            case XZ: {
                this.parametric = new Parametric() {
                    @Override
                    public double x(double t) {
                        return Math.cos(t) * r + x;
                    }

                    @Override
                    public double y(double t) {
                        return y;
                    }

                    @Override
                    public double z(double t) {
                        return Math.sin(t) * r + z;
                    }
                };
                break;
            }
            case YZ: {
                this.parametric = new Parametric() {
                    @Override
                    public double x(double t) {
                        return x;
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
            }
        }
    }

    @Override
    public void draw(ParticleSystem system, Particle particle, DustOptions data) {
        system.parametric(particle, this.parametric, 0, Math.PI * 2, Math.PI / (16 * r), 1, data);
    }

    @Override
    public void drawAbsolute(ParticleSystem system, Particle particle, DustOptions data) {
        system.parametricAbsolute(particle, this.parametric, 0, Math.PI * 2, Math.PI / (16 * r), 1, data);
    }
}
