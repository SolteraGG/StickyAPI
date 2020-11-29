package com.dumbdogdiner.stickyapi.bukkit.particle;

/**
 * A Parametric in u,v,w form
 * @see Parametric for more info
 */
public interface IntermediateParametric {
    double u(double t);
    double v(double t);
    double w(double t);
}
