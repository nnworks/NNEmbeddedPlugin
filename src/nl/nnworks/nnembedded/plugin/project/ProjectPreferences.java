package nl.nnworks.nnembedded.plugin.project;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.service.prefs.BackingStoreException;

import nl.nnworks.nnembedded.plugin.NNEmEmbeddedPlugin;

public class ProjectPreferences {

  private IProject project;
  private HashMap<String, Preference> map = new HashMap<>();

  public ProjectPreferences(IProject project) {
    this.project = project;
  }

  public String get(Object key) {
    return get(key.toString(), "");
  }

  /**
   * Get the preference referred to by <key>
   * 
   * @param key
   * @param defaultValue In case no value is found, this value will be returned
   * @return value referred to by <key> or the default value if no value is found.
   */
  public String get(String key, String defaultValue) {
    if (!map.containsKey(key)) {
      map.put(key, new Preference(getPreference(key.toString(), defaultValue), false));
    }

    return map.get(key).value;
  }

  /**
   * Stores the value under <key>. This value is not persisted until <saveProjectPreferences> is called.
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
  
  /**
   * Saves all changed values to the project preferences for this plug-in
   */
  public void saveProjectPreferences() {
    IScopeContext projectScope = new ProjectScope(project);
    IEclipsePreferences preferences = projectScope.getNode(NNEmEmbeddedPlugin.PLUGIN_ID);

    for (Map.Entry<String, ProjectPreferences.Preference> entry : map.entrySet()) {
      Preference preference = entry.getValue();
      if (preference != null && preference.dirty) {
        preferences.put(entry.getKey(), preference.value);
      }
    }

    try {
      preferences.flush();
    } catch (BackingStoreException e) {
      OperationStatus status = new OperationStatus(IStatus.ERROR, NNEmEmbeddedPlugin.PLUGIN_ID, 3, "Something went wrong while saving preferences to project " + project.getName(), e);
      StatusManager.getManager().handle(status, StatusManager.LOG);
    }
  }
  
  /**
   * Removes all dirty preferences, so they will be read again from the plug-in project file.
   */
  public void restoreProjectPreferences() {
    for (Map.Entry<String, ProjectPreferences.Preference> entry : map.entrySet()) {
      Preference preference = entry.getValue();
      if (preference != null && preference.dirty) {
        map.remove(entry.getKey());
      }
    }
  }

  private String getPreference(String key, String defaultValue) {
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
