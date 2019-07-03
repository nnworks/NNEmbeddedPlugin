package nl.nnworks.nnembedded.plugin.project.builder;

import java.util.Map;

import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import nl.nnworks.nnembedded.plugin.project.NNEmbeddedProject;
import nl.nnworks.nnembedded.plugin.project.ProjectPreferences;
import nl.nnworks.nnembedded.plugin.utils.DateTime;

public class ProjectConfigBuilder extends IncrementalProjectBuilder {

  public static final String BUILDER_ID = "nl.nnworks.nnembedded.plugin.projectconfigbuilder";

  @Override
  protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
    IProject project = getProject();

    System.out.println("ProjectConfigBuilder build triggered for " + project.getName());
    
    switch (kind) {
      case FULL_BUILD:
        fullBuild(monitor);
        break;
      case INCREMENTAL_BUILD:
        incrementalBuild(monitor);
        break;
      case AUTO_BUILD:
        incrementalBuild(monitor);
        break;
    }
    return null;
  }

  protected void fullBuild(final IProgressMonitor monitor) {
    final IProject project = getProject();

    System.out.println("performing full build");
    IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
    buildInfo.getConfigurationNames();
    
    configureCDTProject();
  }

  protected void incrementalBuild(final IProgressMonitor monitor) {
    final IProject project = getProject();
    IResourceDelta delta = getDelta(project);
    if (delta == null) {
      fullBuild(monitor);
    } else {
      // do an incremental build
      System.out.println("performing incremental build");
      // check whether the project descriptions has changed since the last build.
      // if...
      configureCDTProject();
    }
  }

  protected void startupOnInitialize() {
    // add builder init logic here
  }

  protected void clean(IProgressMonitor monitor) {
    // add builder clean logic here
  }

  protected void configureCDTProject() {
    updateLastProjectConfigUpdateTimeStamp();
  }
  
  protected void updateLastProjectConfigUpdateTimeStamp() {
    NNEmbeddedProject project = NNEmbeddedProject.getNNEmbeddedProject(getProject());
    project.getPreferences().put(ProjectPreferences.LAST_PROJECTCONFIGUPDATE_TS, DateTime.getCurrentUTCTime());
    project.getPreferences().saveProjectPreferences();
  }
}
