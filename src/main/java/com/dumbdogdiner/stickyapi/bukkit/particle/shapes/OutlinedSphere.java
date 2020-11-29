/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.particle.shapes;

import com.dumbdogdiner.stickyapi.bukkit.particle.*;
import javafx.geometry.Point3D;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class OutlinedSphere implements Shape {


    private Point3D center;
    private double radius;

    public static final int MIN_DIVISIONS = 4;
    public static final int MAX_DIVISIONS = 32;
    public static final int MAX_RADIUS = 128;


    private final IntermediateParametric genericSphere = new IntermediateParametric() {
        @Override
        public double u(double t) {
            return Math.cos(t) * radius;
        }

        @Override
        public double v(double t) {
            return Math.sin(t) * radius;
        }

        @Override
        public double w(double t) {
            return Math.floor(t / (2 * Math.PI)) - (radius / 2D);
        }
    };

    private Parametric parametric;


    public OutlinedSphere(double x, double y, double z, double radius, int divisions, Orientation orientation) {
        if (radius > MAX_RADIUS) {
            throw new IllegalArgumentException("Tried to draw sphere with absurd radius (>" + MAX_RADIUS + ")!");
        }
        if (radius < MIN_DIVISIONS || radius > MAX_DIVISIONS) {
            throw new IllegalArgumentException("Tried to draw sphere with invalid divisions (>"
                    + MAX_DIVISIONS + " or <" + MIN_DIVISIONS + ")!");
        }


        this.parametric = getParametric(divisions, orientation);


    }

    private Parametric getParametric(int divisions, Orientation orientation){
        switch (orientation) {
            case XY:
                 return new Parametric() {

                    @Override
                    public double x(double t) {
                        return genericSphere.u(t)+center.getX();
                    }

                    @Override
                    public double y(double t) {
                        return genericSphere.v(t)+center.getY();
                    }

                    @Override
                    public double z(double t) {
                        return genericSphere.w(t)+center.getZ();
                    }
                };
            case XZ:
                return new Parametric() {
                    @Override
                    public double x(double t) {
                        return genericSphere.u(t)+ center.getX();
                    }

                    @Override
                    public double y(double t) {
                        return genericSphere.w(t) + center.getY();
                    }

                    @Override
                    public double z(double t) {
                        return genericSphere.v(t) + center.getZ();
                    }
                };
            case YZ:
                return new Parametric() {
                    @Override
                    public double x(double t) {
                        return Math.floor(t / (2 * Math.PI)) - (radius / 2D) + center.getX();
                    }

                    @Override
                    public double y(double t) {
                        return Math.cos(t) * radius + center.getY();
                    }

                    @Override
                    public double z(double t) {
                        return Math.sin(t) * radius + center.getZ();
                    }
                };
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void draw(ParticleSystem system, Particle particle, Particle.DustOptions data) {

    }

    @Override
    public void drawAbsolute(ParticleSystem system, Particle particle, Particle.DustOptions data) {

    }
}
