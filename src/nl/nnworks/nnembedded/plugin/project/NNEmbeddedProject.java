package nl.nnworks.nnembedded.plugin.project;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;

public class NNEmbeddedProject {

  private static final HashMap<IProject, NNEmbeddedProject> instances = new HashMap<IProject, NNEmbeddedProject>();
  private ProjectPreferences preferences;
  
  public NNEmbeddedProject(IProject project) {
    preferences = new ProjectPreferences(project);
  }
  
  /**
   * Gets the NNEmbeddedProject associated with the given IProject instance. Not thread safe, and assumes not concurrency... 
   * @param project
   * @return the NNEmbeddedProject that is linked to the IProject instance 
   */
  public static NNEmbeddedProject getNNEmbeddedProject(IProject project) {
    if (!instances.containsKey(project)) {
      instances.put(project, new NNEmbeddedProject(project));
    }
    return instances.get(project);
  }
  
  public ProjectPreferences getPreferences() {
    return preferences;
  }
}
