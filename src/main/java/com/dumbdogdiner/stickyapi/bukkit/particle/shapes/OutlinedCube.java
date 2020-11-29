/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.particle.shapes;

import com.dumbdogdiner.stickyapi.bukkit.particle.ParticleSystem;
import com.dumbdogdiner.stickyapi.bukkit.particle.Shape;
import javafx.geometry.Point3D;
import lombok.Getter;
import org.bukkit.Particle;

import java.util.*;


public class OutlinedCube implements Shape {
    @Getter
    private final double inscribedRadius;
    @Getter
    private final Point3D center;


    /**
     * Draws a cube out of particles
     * @param center the center of the cube
     * @param inscribedRadius the radius of a sphere inscribed in the cube
     */
    public OutlinedCube(Point3D center, double inscribedRadius){
        this.center = center;
        this.inscribedRadius = inscribedRadius;
    }

    public OutlinedCube(double x1, double y1, double z1, double inscribedRadius){
        this(new Point3D(x1, y1, z1), inscribedRadius);
    }

    private double getSideLength(){
        return (2D*Math.sqrt(3D)*inscribedRadius)/3D;
    }

    private Map<Point3D, Point3D []> getVertices(){
        HashMap<Point3D, Point3D []> adjacencies = new HashMap<>();
        double side = getSideLength();
        double halfSide = side / 2D;

        Point3D [] magicVerticies = new Point3D[4];

        magicVerticies[0] = center.add(halfSide, halfSide, halfSide);
        magicVerticies[1] = center.add(-halfSide, halfSide, -halfSide);
        magicVerticies[2] = center.add(-halfSide, -halfSide, halfSide);
        magicVerticies[3] = center.add(halfSide, -halfSide, -halfSide);

        ArrayList<Double> mask = new ArrayList<>(List.of(side,0D,0D));
        Point3D [] adj;

        for (Point3D magicVerticy : magicVerticies) {
            double[] sign = new double[]{
                    -1 * Math.signum(magicVerticy.getX()),
                    -1 * Math.signum(magicVerticy.getY()),
                    -1 * Math.signum(magicVerticy.getZ())};
            adj = new Point3D[3];
            for (int i = 0; i < mask.size(); i++) {
                adj[i] = magicVerticy.add(mask.get(0) * sign[0], mask.get(1) * sign[1], mask.get(2) * sign[2]);
                Collections.rotate(mask, 1);
            }
            adjacencies.put(magicVerticy, adj);
        }
        return adjacencies;
    }


    @Override
    public void draw(ParticleSystem system, Particle particle, Particle.DustOptions data) {
        Map<Point3D, Point3D []> vertexAdj = getVertices();
        for(Point3D p1 : vertexAdj.keySet())
            for(Point3D p2 : vertexAdj.get(p1))
                system.line(particle, p1, p2, 1, (int) Math.ceil(16 * getSideLength()), data);
    }

    @Override
    public void drawAbsolute(ParticleSystem system, Particle particle, Particle.DustOptions data) {
        Map<Point3D, Point3D []> vertexAdj = getVertices();
        for(Point3D p1 : vertexAdj.keySet())
            for(Point3D p2 : vertexAdj.get(p1))
                system.lineAbsolute(particle, p1, p2, 1, (int) Math.ceil(16 * getSideLength()), data);
    }
}
