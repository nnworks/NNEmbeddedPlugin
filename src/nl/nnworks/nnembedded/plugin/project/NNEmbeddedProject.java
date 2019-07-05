package nl.nnworks.nnembedded.plugin.project;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;

public class NNEmbeddedProject {

  private static final HashMap<IProject, NNEmbeddedProject> instances = new HashMap<IProject, NNEmbeddedProject>();
  private NNEmbeddedProjectPreferences preferences;
  private final IProject project;
  
  public NNEmbeddedProject(final IProject project) {
    this.project = project;
    preferences = new NNEmbeddedProjectPreferences(project);
  }
  
  /**
   * Gets the NNEmbeddedProject associated with the given IProject instance. Not thread safe, and assumes not concurrency... 
   * @param project
   * @return the NNEmbeddedProject that is linked to the IProject instance 
   */
  public static NNEmbeddedProject getNNEmbeddedProject(final IProject project) {
    if (!instances.containsKey(project)) {
      instances.put(project, new NNEmbeddedProject(project));
    }
    return instances.get(project);
  }
  
  public NNEmbeddedProjectPreferences getPreferences() {
    return preferences;
  }
  
  public File getFile(final String filename) {
    if (filename != null) {
      File file = new File(filename);
      if (file.isAbsolute()) {
        return file;
      } else {
        // prepend project path
        return new File(project.getLocation().addTrailingSeparator().toString() + filename);
      }
    } else {
      return null;
    }
    
  }
}
