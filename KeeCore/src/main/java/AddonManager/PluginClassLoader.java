package AddonManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.plugin.InvalidPluginException;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
final class PluginClassLoader extends URLClassLoader {
    final DragonPlugin plugin;
    PluginClassLoader(final ClassLoader parent, String main, final File file) throws InvalidPluginException, MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(main, true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + main + "'", ex);
            }

            Class<? extends DragonPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(DragonPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + main + "' does not extend DragonAddon", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

}
