package nl.nnworks.nnembedded.plugin;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The activator class controls the plug-in life cycle
 */
public class NNEmEmbeddedPlugin extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "NNEmbedded"; //$NON-NLS-1$

  // The shared instance
  private static NNEmEmbeddedPlugin plugin;

  /**
   * The constructor
   */
  public NNEmEmbeddedPlugin() {
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
    System.out.println("Activated");
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static NNEmEmbeddedPlugin getDefault() {
    return plugin;
  }

  public void saveProjectPreference(final String key, final String value) {
    IScopeContext projectScope = new ProjectScope(propertiesPage.getProject());
    
    IEclipsePreferences preferences = projectScope.getNode(NNEmEmbeddedPlugin.PLUGIN_ID);
    preferences.put(key, value);
    try {
      preferences.flush();
    } catch (BackingStoreException e) {
      OperationStatus status = new OperationStatus(IStatus.ERROR, NNEmEmbeddedPlugin.PLUGIN_ID, 3, "Something went wrong while saving preference " + key + " to project " + propertiesPage.getProject().getName(), e);
      StatusManager.getManager().handle(status, StatusManager.LOG);
    }
  }
  
  public void getCurrentPreferences() {
    IScopeContext projectScope = new ProjectScope(propertiesPage.getProject());
    
    IEclipsePreferences preferences = projectScope.getNode(NNEmEmbeddedPlugin.PLUGIN_ID);
    prefLastBrowseDir = preferences.get("last-browse-dir", System.getProperty("user.home"));
  }
  
  
}
