package nl.nnworks.nnembedded.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

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
  
}
