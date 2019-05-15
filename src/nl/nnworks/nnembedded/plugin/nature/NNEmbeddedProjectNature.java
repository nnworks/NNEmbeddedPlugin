package nl.nnworks.nnembedded.plugin.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class NNEmbeddedProjectNature implements IProjectNature {

  public static final String NATURE_ID = "nl.nnworks.nnembedded.plugin.nature.nnembeddedprojectnature";
  public static final String CNATURE_ID = "org.eclipse.cdt.core.cnature";
  public static final String CCNATURE_ID = "org.eclipse.cdt.core.ccnature";

  private IProject project;

  /**
     * Called when this nature is added to a project 
     */
	@Override
	public void configure() throws CoreException {
		System.out.println("configure nature");
	}

  /**
   * Called when this nature is removed from a project
   */
  @Override
  public void deconfigure() throws CoreException {
    System.out.println("deconfigure nature");
  }

  @Override
  public IProject getProject() {
    return project;
  }

  @Override
  public void setProject(IProject project) {
    System.out.println("set project on nature");
    this.project = project;
  }

}
