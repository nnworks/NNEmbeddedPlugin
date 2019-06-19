package nl.nnworks.nnembedded.plugin.project.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ProjectConfigBuilder extends IncrementalProjectBuilder {

  public static final String BUILDER_ID = "nl.nnworks.nnembedded.plugin.projectconfigbuilder";
  
  @Override
  protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
    IProject project = getProject();

    switch (kind) {
      case FULL_BUILD:
        System.out.println("ProjectConfigBuilder full build triggered for " + project.getName());
        break;
      case INCREMENTAL_BUILD:
        System.out.println("ProjectConfigBuilder incremental build triggered for " + project.getName());
        break;
      case AUTO_BUILD:
        System.out.println("ProjectConfigBuilder auto build triggered for " + project.getName());
        break;
    }
    return null;
  }

}
