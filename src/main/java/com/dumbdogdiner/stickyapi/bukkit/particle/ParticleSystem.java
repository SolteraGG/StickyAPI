/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the spawning and animation of particles.
 * 
 * @since 2.0
 */
public class ParticleSystem {
    /**
     * The plugin this particle system is attached to.
     */
    @Getter
    @NotNull
    private final Plugin plugin;

    /**
     * The world this particle system is contained in.
     */
    @Getter
    @NotNull
    private final World world;

    /**
     * The relative root of this system. Calling non-absolute methods target the
     * system to spawn relative to this location.
     */
    @Getter
    private Location root;

    /**
     * Set the relative root of this system.
     * 
     * @param root The new relative root
     */
    public void setRoot(@NotNull Location root) {
        if (!root.getWorld().equals(world)) {
            throw new IllegalArgumentException("New root must be contained within the system's original world.");
        }
        this.root = root;
    }

    /**
     * The default particle to use when drawing.
     */
    @Getter
    @Setter
    @Nullable
    private Particle particle;

    /**
     * The default dust options to use when drawing.
     */
    @Getter
    @Setter
    @Nullable
    private Particle.DustOptions data;

    /**
     * Create a new particle system using absolute co-ordinates.
     * 
     * @param plugin The plugin which owns this system
     * @param world  The world this system belongs to
     */
    public ParticleSystem(@NotNull Plugin plugin, @NotNull World world) {
        this.plugin = plugin;
        this.world = world;
    }

    /**
     * Create a new particle system based around the specified location.
     * 
     * @param plugin The plugin which owns this system
     * @param root   The root location of this system
     */
    public ParticleSystem(@NotNull Plugin plugin, @NotNull Location root) {
        this.plugin = plugin;
        this.world = root.getWorld();
        this.root = root;
    }

    /**
     * Returns whether or not this system is using absolute co-ordinates.
     * 
     * @return {@link Boolean}
     */
    @NotNull
    public Boolean isAbsolute() {
        return this.root != null;
    }

    /**
     * Ensures that this.root is set.
     */
    private void ensureRelative() {
        if (!isAbsolute())
            throw new RuntimeException("Cannot use relative methods - system is absolute");
    }

    /**
     * Ensures that this.particle and this.data are set.
     */
    private void ensureDefaultParticle() {
        if (this.particle == null)
            throw new RuntimeException("Cannot use default particle - particle is null");
    }

    /**
     * Spawn particles at the target co-ordinates, relative to the root of the
     * system. Summoning particles at (0,0,0) refers to (root_x, root_y, root_z)
     * relative to world origin.
     * 
     * @param particle The type of particle to spawn
     * @param x        The x co-ordinate relative to the system root
     * @param y        The y co-ordinate relative to the system root
     * @param z        The z co-ordinate relative to the system root
     * @param count    The number of particles to spawn
     * @param data     Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem spawn(@NotNull Particle particle, double x, double y, double z, int count,
            @Nullable Particle.DustOptions data) {
        this.ensureRelative();
        this.world.spawnParticle(particle, this.root.getX() + x, this.root.getY() + y, this.root.getZ() + z, count,
                data);
        return this;
    }

    /**
     * Spawn particles at the target co-ordinates, relative to the root of the
     * system. Summoning particles at (0,0,0) refers to (root_x, root_y, root_z)
     * relative to world origin.
     * 
     * @param particle The type of particle to spawn
     * @param x        The x co-ordinate relative to the system root
     * @param y        The y co-ordinate relative to the system root
     * @param z        The z co-ordinate relative to the system root
     * @param count    The number of particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem spawn(@NotNull Particle particle, double x, double y, double z, int count) {
        return this.spawn(particle, x, y, z, count, null);
    }

    /**
     * Spawn particles at the target co-ordinates, relative to the root of the
     * system. Summoning particles at (0,0,0) refers to (root_x, root_y, root_z)
     * relative to world origin.
     * 
     * @param x     The x co-ordinate relative to the system root
     * @param y     The y co-ordinate relative to the system root
     * @param z     The z co-ordinate relative to the system root
     * @param count The number of particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem spawn(double x, double y, double z, int count) {
        this.ensureDefaultParticle();
        assert this.particle != null;
        return this.spawn(this.particle, x, y, z, count, this.data);
    }

    /**
     * Spawn particles at the absolute target co-ordinates. Summoning particles at
     * (0,0,0) refers to (0, 0, 0) relative to world origin.
     * 
     * @param particle The type of particle to spawn
     * @param x        The absolute x co-ordinate
     * @param y        The absolute y co-ordinate
     * @param z        The absolute z co-ordinate
     * @param count    The number of particles to spawn
     * @param data     Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem spawnAbsolute(@NotNull Particle particle, double x, double y, double z, int count,
            @Nullable Particle.DustOptions data) {
        this.world.spawnParticle(particle, x, y, z, count, data);
        return this;
    }

    /**
     * Spawn particles at the absolute target co-ordinates. Summoning particles at
     * (0,0,0) refers to (0, 0, 0) relative to world origin.
     * 
     * @param particle The type of particle to spawn
     * @param x        The absolute x co-ordinate
     * @param y        The absolute y co-ordinate
     * @param z        The absolute z co-ordinate
     * @param count    The number of particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem spawnAbsolute(@NotNull Particle particle, double x, double y, double z, int count) {
        return this.spawnAbsolute(particle, x, y, z, count, null);
    }

    /**
     * Spawn particles at the absolute target co-ordinates. Summoning particles at
     * (0,0,0) refers to (0, 0, 0) relative to world origin.
     * 
     * @param x     The absolute x co-ordinate
     * @param y     The absolute y co-ordinate
     * @param z     The absolute z co-ordinate
     * @param count The number of particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem spawnAbsolute(double x, double y, double z, int count) {
        this.ensureDefaultParticle();
        return this.spawnAbsolute(this.particle, x, y, z, count, this.data);
    }

    /**
     * Spawn particles in the shape of a parametric curve. Summoning particles at
     * (0,0,0) refers to (root_x, root_y, root_z) relative to world origin.
     * 
     * @param particle   The type of particle to spawn
     * @param parametric The curve to follow
     * @param t0         The initial value of t
     * @param t1         The final value of t
     * @param stepSize   The size of steps made in-between t0 and t1
     * @param count      The number of particles to spawn per stepSize
     * @param data       Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem parametric(@NotNull Particle particle, @NotNull Parametric parametric, double t0, double t1,
            double stepSize, int count, @Nullable Particle.DustOptions data) {
        this.ensureRelative();
        for (var t = t0; t < t1; t += stepSize) {
            this.spawn(particle, parametric.x(t), parametric.y(t), parametric.z(t), count, data);
        }
        return this;
    }

    /**
     * Spawn particles in the shape of a parametric curve. Summoning particles at
     * (0,0,0) refers to (root_x, root_y, root_z) relative to world origin.
     * 
     * @param particle   The type of particle to spawn
     * @param parametric The curve to follow
     * @param t0         The initial value of t
     * @param t1         The final value of t
     * @param stepSize   The size of steps made in-between t0 and t1
     * @param count      The number of particles to spawn per stepSize
     * @return {@link ParticleSystem}
     */
    public ParticleSystem parametric(@NotNull Particle particle, @NotNull Parametric parametric, double t0, double t1,
            double stepSize, int count) {
        return this.parametric(particle, parametric, t0, t1, stepSize, count, null);
    }

    /**
     * Spawn particles in the shape of a parametric curve. Summoning particles at
     * (0,0,0) refers to (root_x, root_y, root_z) relative to world origin.
     * 
     * @param parametric The curve to follow
     * @param t0         The initial value of t
     * @param t1         The final value of t
     * @param stepSize   The size of steps made in-between t0 and t1
     * @param count      The number of particles to spawn per stepSize
     * @return {@link ParticleSystem}
     */
    public ParticleSystem parametric(@NotNull Parametric parametric, double t0, double t1, double stepSize, int count) {
        this.ensureDefaultParticle();
        return this.parametric(this.particle, parametric, t0, t1, stepSize, count, this.data);
    }

    /**
     * Spawn particles in the shape of a parametric curve using absolute
     * co-ordinates. Summoning particles at (0,0,0) refers to (0, 0, 0) relative to
     * world origin.
     * 
     * @param particle   The type of particle to spawn
     * @param parametric The curve to follow
     * @param t0         The initial value of t
     * @param t1         The final value of t
     * @param stepSize   The size of steps made in-between t0 and t1
     * @param count      The number of particles to spawn per stepSize
     * @param data       Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem parametricAbsolute(@NotNull Particle particle, @NotNull Parametric parametric, double t0,
            double t1, double stepSize, int count, @Nullable Particle.DustOptions data) {
        for (var t = t0; t < t1; t += stepSize) {
            this.spawnAbsolute(particle, parametric.x(t), parametric.y(t), parametric.z(t), count, data);
        }
        return this;
    }

    /**
     * Spawn particles in the shape of a parametric curve using absolute
     * co-ordinates. Summoning particles at (0,0,0) refers to (0, 0, 0) relative to
     * world origin.
     * 
     * @param particle   The type of particle to spawn
     * @param parametric The curve to follow
     * @param t0         The initial value of t
     * @param t1         The final value of t
     * @param stepSize   The size of steps made in-between t0 and t1
     * @param count      The number of particles to spawn per stepSize
     * @return {@link ParticleSystem}
     */
    public ParticleSystem parametricAbsolute(@NotNull Particle particle, @NotNull Parametric parametric, double t0,
            double t1, double stepSize, int count) {
        return this.parametricAbsolute(particle, parametric, t0, t1, stepSize, count, null);
    }

    /**
     * Spawn particles in the shape of a parametric curve using absolute
     * co-ordinates. Summoning particles at (0,0,0) refers to (0, 0, 0) relative to
     * world origin.
     * 
     * @param parametric The curve to follow
     * @param t0         The initial value of t
     * @param t1         The final value of t
     * @param stepSize   The size of steps made in-between t0 and t1
     * @param count      The number of particles to spawn per stepSize
     * @return {@link ParticleSystem}
     */
    public ParticleSystem parametricAbsolute(Parametric parametric, double t0, double t1, double stepSize, int count) {
        this.ensureDefaultParticle();
        return this.parametric(this.particle, parametric, t0, t1, stepSize, count, this.data);
    }

    // TODO: change these to use 3D points or something similar, for fewer arguments
    // and better readability, or add a helper method to do the same
    /**
     * Draw a straight line between the specified relative co-ordinates.
     * 
     * @param particle The type of particle to spawn
     * @param x1       The x co-ordinate of point a relative to the system root
     * @param y1       The y co-ordinate of point a relative to the system root
     * @param z1       The z co-ordinate of point a relative to the system root
     * @param x2       The x co-ordinate of point b relative to the system root
     * @param y2       The y co-ordinate of point b relative to the system root
     * @param z2       The z co-ordinate of point b relative to the system root
     * @param steps    The number of steps to take drawing the line
     * @param count    The number of particles to spawn per step
     * @param data     Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem line(@NotNull Particle particle, double x1, double y1, double z1, double x2, double y2,
            double z2, double steps, int count, @Nullable Particle.DustOptions data) {
        this.ensureRelative();
        return this.parametric(particle, new Parametric() {
            public double x(double t) {
                return x2 * t + (1 - t) * x1;
            }

            public double y(double t) {
                return y2 * t + (1 - t) * y1;
            }

            public double z(double t) {
                return z2 * t + (1 - t) * z1;
            }
        }, 0, 1, 1 / steps, count);
    }

    /**
     * Draw a straight line between the specified relative co-ordinates.
     * 
     * @param particle The type of particle to spawn
     * @param x1       The x co-ordinate of point a relative to the system root
     * @param y1       The y co-ordinate of point a relative to the system root
     * @param z1       The z co-ordinate of point a relative to the system root
     * @param x2       The x co-ordinate of point b relative to the system root
     * @param y2       The y co-ordinate of point b relative to the system root
     * @param z2       The z co-ordinate of point b relative to the system root
     * @param steps    The number of steps to take drawing the line
     * @param count    The number of particles to spawn per step
     * @return {@link ParticleSystem}
     */
    public ParticleSystem line(@NotNull Particle particle, double x1, double y1, double z1, double x2, double y2,
            double z2, double steps, int count) {
        return this.line(particle, x1, y1, z1, x2, y2, z2, steps, count, null);
    }

    /**
     * Draw a straight line between the specified relative co-ordinates.
     * 
     * @param x1    The x co-ordinate of point a relative to the system root
     * @param y1    The y co-ordinate of point a relative to the system root
     * @param z1    The z co-ordinate of point a relative to the system root
     * @param x2    The x co-ordinate of point b relative to the system root
     * @param y2    The y co-ordinate of point b relative to the system root
     * @param z2    The z co-ordinate of point b relative to the system root
     * @param steps The number of steps to take drawing the line
     * @param count The number of particles to spawn per step
     * @return {@link ParticleSystem}
     */
    public ParticleSystem line(double x1, double y1, double z1, double x2, double y2, double z2, double steps,
            int count) {
        this.ensureDefaultParticle();
        return this.line(this.particle, x1, y1, z1, x2, y2, z2, steps, count, this.data);
    }

    /**
     * Draw a straight line between the specified absolute co-ordinates.
     * 
     * @param particle The type of particle to spawn
     * @param x1       The x co-ordinate of point a relative to the world origin
     * @param y1       The y co-ordinate of point a relative to the world origin
     * @param z1       The z co-ordinate of point a relative to the world origin
     * @param x2       The x co-ordinate of point b relative to the world origin
     * @param y2       The y co-ordinate of point b relative to the world origin
     * @param z2       The z co-ordinate of point b relative to the world origin
     * @param steps    The number of steps to take drawing the line
     * @param count    The number of particles to spawn per step
     * @param data     Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem lineAbsolute(@NotNull Particle particle, double x1, double y1, double z1, double x2,
            double y2, double z2, double steps, int count, @Nullable Particle.DustOptions data) {
        return this.parametricAbsolute(particle, new Parametric() {
            public double x(double t) {
                return x2 * t + (1 - t) * x1;
            }

            public double y(double t) {
                return y2 * t + (1 - t) * y1;
            }

            public double z(double t) {
                return z2 * t + (1 - t) * z1;
            }
        }, 0, 1, 1 / steps, count, data);
    }

    /**
     * Draw a straight line between the specified absolute co-ordinates.
     * 
     * @param particle The type of particle to spawn
     * @param x1       The x co-ordinate of point a relative to the world origin
     * @param y1       The y co-ordinate of point a relative to the world origin
     * @param z1       The z co-ordinate of point a relative to the world origin
     * @param x2       The x co-ordinate of point b relative to the world origin
     * @param y2       The y co-ordinate of point b relative to the world origin
     * @param z2       The z co-ordinate of point b relative to the world origin
     * @param steps    The number of steps to take drawing the line
     * @param count    The number of particles to spawn per step
     * @return {@link ParticleSystem}
     */
    public ParticleSystem lineAbsolute(@NotNull Particle particle, double x1, double y1, double z1, double x2,
            double y2, double z2, double steps, int count) {
        return this.lineAbsolute(particle, x1, y1, z1, x2, y2, z2, steps, count, null);
    }

    /**
     * Draw a straight line between the specified absolute co-ordinates.
     * 
     * @param x1    The x co-ordinate of point a relative to the world origin
     * @param y1    The y co-ordinate of point a relative to the world origin
     * @param z1    The z co-ordinate of point a relative to the world origin
     * @param x2    The x co-ordinate of point b relative to the world origin
     * @param y2    The y co-ordinate of point b relative to the world origin
     * @param z2    The z co-ordinate of point b relative to the world origin
     * @param steps The number of steps to take drawing the line
     * @param count The number of particles to spawn per step
     * @return {@link ParticleSystem}
     */
    public ParticleSystem lineAbsolute(double x1, double y1, double z1, double x2, double y2, double z2, double steps,
            int count) {
        this.ensureDefaultParticle();
        return this.lineAbsolute(this.particle, x1, y1, z1, x2, y2, z2, steps, count, this.data);
    }

    /**
     * Draw a straight line between the specified absolute co-ordinates.
     * 
     * @param particle The type of particle to spawn
     * @param a        The start location
     * @param b        The end location
     * @param steps    The number of steps to take drawing the line
     * @param count    The number of particles to spawn per step
     * @param data     Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem lineAbsolute(Particle particle, Location a, Location b, double steps, int count,
            Particle.DustOptions data) {
        return this.lineAbsolute(particle, a.getX(), a.getY(), a.getZ(), b.getX(), b.getY(), b.getZ(), steps, count,
                data);
    }

    /**
     * Draw a straight line between the specified absolute co-ordinates.
     * 
     * @param particle The type of particle to spawn
     * @param a        The start location
     * @param b        The end location
     * @param steps    The number of steps to take drawing the line
     * @param count    The number of particles to spawn per step
     * @return {@link ParticleSystem}
     */
    public ParticleSystem lineAbsolute(Particle particle, Location a, Location b, double steps, int count) {
        return this.lineAbsolute(particle, a.getX(), a.getY(), a.getZ(), b.getX(), b.getY(), b.getZ(), steps, count,
                null);
    }

    /**
     * Draw a straight line between the specified absolute co-ordinates.
     * 
     * @param a     The start location
     * @param b     The end location
     * @param steps The number of steps to take drawing the line
     * @param count The number of particles to spawn per step
     * @return {@link ParticleSystem}
     */
    public ParticleSystem lineAbsolute(Location a, Location b, double steps, int count) {
        this.ensureDefaultParticle();
        return this.lineAbsolute(this.particle, a.getX(), a.getY(), a.getZ(), b.getX(), b.getY(), b.getZ(), steps,
                count, this.data);
    }

    /**
     * Draws a shape.
     * 
     * @param particle The type of particle to spawn
     * @param shape    The shape
     * @param data     Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem shape(@NotNull Particle particle, @NotNull Shape shape, @Nullable Particle.DustOptions data) {
        this.ensureRelative();
        shape.draw(this, particle, data);
        return this;
    }

    /**
     * Draws a shape.
     * 
     * @param particle The type of particle to spawn
     * @param shape    The shape
     * @return {@link ParticleSystem}
     */
    public ParticleSystem shape(@NotNull Particle particle, @NotNull Shape shape) {
        return this.shape(particle, shape, null);
    }

    /**
     * Draws a shape.
     * 
     * @param shape The shape
     * @return {@link ParticleSystem}
     */
    public ParticleSystem shape(Shape shape) {
        this.ensureDefaultParticle();
        assert this.particle != null;
        return this.shape(this.particle, shape, this.data);
    }

    /**
     * Draws a shape.
     * 
     * @param particle The type of particle to spawn
     * @param shape    The shape
     * @param data     Data of the particles to spawn
     * @return {@link ParticleSystem}
     */
    public ParticleSystem shapeAbsolute(@NotNull Particle particle, @NotNull Shape shape,
            @Nullable Particle.DustOptions data) {
        ;
        shape.drawAbsolute(this, particle, data);
        return this;
    }

    /**
     * Draws a shape.
     * 
     * @param particle The type of particle to spawn
     * @param shape    The shape
     * @return {@link ParticleSystem}
     */
    public ParticleSystem shapeAbsolute(@NotNull Particle particle, @NotNull Shape shape) {
        return this.shapeAbsolute(particle, shape, null);
    }

    /**
     * Draws a shape.
     * 
     * @param shape The shape
     * @return {@link ParticleSystem}
     */
    public ParticleSystem shapeAbsolute(@NotNull Shape shape) {
        this.ensureDefaultParticle();
        assert this.particle != null;
        return this.shapeAbsolute(this.particle, shape, this.data);
    }
}
