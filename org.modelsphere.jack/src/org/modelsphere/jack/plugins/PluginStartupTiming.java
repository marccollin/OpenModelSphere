package org.modelsphere.jack.plugins;

import java.util.Locale;

/** Opt-in timing of plugin startup phases, enabled with -Dmodelsphere.plugins.timing=true. */
public final class PluginStartupTiming implements AutoCloseable {

    private static final boolean ENABLED = Boolean.getBoolean("modelsphere.plugins.timing");
    private static final PluginStartupTiming DISABLED = new PluginStartupTiming(null, null);

    private final String phase;
    private final String plugin;
    private final long started;

    private PluginStartupTiming(String phase, String plugin) {
        this.phase = phase;
        this.plugin = plugin;
        this.started = ENABLED ? System.nanoTime() : 0L;
    }

    public static PluginStartupTiming start(String phase) {
        return start(phase, null);
    }

    public static PluginStartupTiming start(String phase, String plugin) {
        return ENABLED ? new PluginStartupTiming(phase, plugin) : DISABLED;
    }

    @Override
    public void close() {
        if (phase == null) {
            return;
        }
        double elapsedMs = (System.nanoTime() - started) / 1_000_000.0;
        System.err.printf(Locale.ROOT, "[plugin-startup] phase=%s elapsed_ms=%.3f%s%n", phase,
                elapsedMs, plugin == null ? "" : " plugin=" + plugin);
    }
}
