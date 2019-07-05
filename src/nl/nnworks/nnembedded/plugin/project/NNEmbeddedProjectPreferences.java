package nl.nnworks.nnembedded.plugin.project;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;

import nl.nnworks.nnembedded.plugin.NNEmEmbeddedPlugin;
import nl.nnworks.nnembedded.plugin.StatusCode;
import nl.nnworks.nnembedded.plugin.utils.StatusLogger;

public class NNEmbeddedProjectPreferences {

  private IProject project;
  private HashMap<String, Preference> map = new HashMap<>();

  /**
   * preference keys
   */
  public static final String LAST_BROWSE_DIR   = "last-browse-dir";
  public static final String FOR_PDESC_FILE    = "pdesc-file";
  public static final String FOR_BUILD_CONFIGS = "for-build-configs";
  public static final String LAST_PROJECTCONFIGUPDATE_TS = "last-config-update-timestamp";
  public static final String DEFAULT_PDESC_FILE = "nnembedded.pdesc";

  public NNEmbeddedProjectPreferences(IProject project) {
    this.project = project;
  }

  /**
   * Get the preference referred to by key. When the key is not found, defaultValue is returned.
   * 
   * @param key
   * @return Value referred to by key or null if the key is not found.
   */
  public String get(Object key) {
    return get(key.toString(), null);
  }

  /**
   * Get the preference referred to by key. When the key is not found, defaultValue is returned.
   * 
   * @param key
   * @param defaultValue In case no key/value is found, this value will be returned.
   * @return Value referred to by key or the default value if the key is not found.
   */
  public String get(String key, String defaultValue) {
    if (!map.containsKey(key)) {
      // read through to prefs file
      String readThroughValue = getPreferenceFromPrefsFile(key, null);

      // if a value is present, cache it in the local map
      if (readThroughValue != null) {
        map.put(key, new Preference(readThroughValue, false));
        return readThroughValue;
      } else {
        return defaultValue;
      }
    } else {
      Preference preference = map.get(key);
      if (preference != null &&preference.value != null) { 
        return map.get(key).value;
      } else {
        // value is null, or not present
        return defaultValue;
      }
    }
  }

  /**
   * Stores the value under key. This value is not persisted until saveProjectPreferences is called.
   * @param key key of the value
   * @param value 
   * @return the previous value, or null
   */
  public String put(String key, String value) {
    String previousValue = null;
    if (map.containsKey(key)) {
      previousValue = map.get(key).value;
    }
    map.put(key, new Preference(value, true));
    
    return previousValue;
  }

  public void remove(final String key) {
    put(key, null);
  }
  
  /**
   * Saves all changed values to the project preferences for this plug-in
   */
  public void saveProjectPreferences() {
    IScopeContext projectScope = new ProjectScope(project);
    IEclipsePreferences preferences = projectScope.getNode(NNEmEmbeddedPlugin.PLUGIN_ID);

    for (Map.Entry<String, NNEmbeddedProjectPreferences.Preference> entry : map.entrySet()) {
      Preference preference = entry.getValue();
      if (preference != null && preference.dirty) {
        if (preference.value == null) {
          preferences.remove(entry.getKey());
        } else {
          preferences.put(entry.getKey(), preference.value);
        }
      }
    }

    try {
      preferences.flush();
    } catch (BackingStoreException e) {
      StatusLogger.LogStatus(IStatus.ERROR, project, StatusCode.ERROR_SAVINGPROPERTIES, "Something went wrong while saving preferences to project", e);
    }
  }
  
  /**
   * Removes all dirty preferences, so they will be read again from the plug-in project file.
   */
  public void restoreProjectPreferences() {
    for (Map.Entry<String, NNEmbeddedProjectPreferences.Preference> entry : map.entrySet()) {
      Preference preference = entry.getValue();
      if (preference != null && preference.dirty) {
        map.remove(entry.getKey());
      }
    }
  }

  private String getPreferenceFromPrefsFile(String key, String defaultValue) {
    IScopeContext projectScope = new ProjectScope(project);

    IEclipsePreferences preferences = projectScope.getNode(NNEmEmbeddedPlugin.PLUGIN_ID);
    return preferences.get(key, defaultValue);
  }

  /**
   * container for value and dirty flag
   */
  protected class Preference {
    public Preference(String value, boolean dirty) {
      this.value = value;
      this.dirty = dirty;
    }

    protected String value;
    protected boolean dirty;
  }
}
