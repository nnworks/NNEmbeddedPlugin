package nl.nnworks.nnembedded.plugin.project.builder;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import nl.nnworks.nnembedded.plugin.project.NNEmbeddedProject;
import nl.nnworks.nnembedded.plugin.project.NNEmbeddedProjectPreferences;
import nl.nnworks.nnembedded.plugin.utils.DateTime;

public class ProjectConfigBuilder extends IncrementalProjectBuilder {

  public static final String BUILDER_ID = "nl.nnworks.nnembedded.plugin.projectconfigbuilder";
  private NNEmbeddedProject nnEmbeddedProject;
  private NNEmbeddedProjectPreferences projectPreferences;

  public ProjectConfigBuilder() {
    super();
  }

  @Override
  protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
    IProject project = getProject();
    nnEmbeddedProject = NNEmbeddedProject.getNNEmbeddedProject(getProject());
    projectPreferences = nnEmbeddedProject.getPreferences();

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
    final IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);

    System.out.println("performing full build");
    buildInfo.getConfigurationNames();

    if (reconfigureOfProjectNeeded(null, buildInfo)) {
      configureCDTProject(buildInfo);
    }
  }

  protected void incrementalBuild(final IProgressMonitor monitor) {
    final IProject project = getProject();
    final IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);

    IResourceDelta delta = getDelta(project);
    if (delta == null) {
      fullBuild(monitor);
    } else {
      // do an incremental build
      System.out.println("performing incremental build");
      // check whether the project needs to be reconfigured due to changed project
      // description file or project file is newer than last updated time stamp.
      if (reconfigureOfProjectNeeded(delta, buildInfo)) {
        configureCDTProject(buildInfo);
      }
    }
  }

  protected void startupOnInitialize() {
    // add builder init logic here
  }

  protected void clean(IProgressMonitor monitor) {
    // add builder clean logic here
  }

  protected void configureCDTProject(final IManagedBuildInfo buildInfo) {
    updateLastProjectConfigUpdateTimeStamp(buildInfo);
  }

  /**
   * Update the time stamp in the project preferences of the last project
   * configuration build to the current UTC time.
   */
  protected void updateLastProjectConfigUpdateTimeStamp(final IManagedBuildInfo buildInfo) {
    projectPreferences.put(String.format("%s-%s", NNEmbeddedProjectPreferences.LAST_PROJECTCONFIGUPDATE_TS, buildInfo.getConfigurationName()), DateTime.getCurrentUTCTimeInRFC1123());
    projectPreferences.saveProjectPreferences();
  }

  /**
   * Check whether the current time stamp is newer that 
   * @return
   */
  protected boolean reconfigureOfProjectNeeded(final IResourceDelta delta, final IManagedBuildInfo buildInfo) {
    final String lastBuildTimeStamp = projectPreferences.get(String.format("%s-%s", NNEmbeddedProjectPreferences.LAST_PROJECTCONFIGUPDATE_TS, buildInfo.getConfigurationName()));
    if (lastBuildTimeStamp == null) {
      return true;
    }

    final String pDescFilename = projectPreferences.get(NNEmbeddedProjectPreferences.FOR_PDESC_FILE, NNEmbeddedProjectPreferences.DEFAULT_PDESC_FILE);
    File pDescFile = nnEmbeddedProject.getFile(pDescFilename);

    if (pDescFile != null) {
      ZonedDateTime zdtModified = DateTime.convertLocalTimeStampToUTC(pDescFile.lastModified());
      ZonedDateTime zdtLastBuild = DateTime.getCurrentUTCTimeFromRFC1123(lastBuildTimeStamp);
      if (zdtModified.isAfter(zdtLastBuild)) {
        return true;
      }

      // check for other files that can make a configuration update needed
      findCriticalResourcesChanged(delta);

    }

    return false;
  }
  
  private String[] findCriticalResourcesChanged(final IResourceDelta delta) {

    if (delta == null) {
      return null;
    }
    
    System.out.printf("Name of resource: %s", delta.getResource().getName());
    
    Stream<IResourceDelta> stream = flattenToStream(Arrays.stream(delta.getAffectedChildren()));

    return stream.map(getCriticalResourceFilter()).collect(Collectors.toList()).toArray(new String[1]);
  }
  
  private Stream<IResourceDelta> flattenToStream(Stream<IResourceDelta> stream) {
    return stream.flatMap((child) -> {
      // return stream if there are affected children
      if (child.getAffectedChildren().length > 0) {
        return flattenToStream(Arrays.stream(child.getAffectedChildren()));
      } else {
        return Stream.empty();
      }
    });
  }
  
  private Function<IResourceDelta, String> getCriticalResourceFilter() {
    Function<IResourceDelta, String> filter = (delta) -> {
      System.out.println(delta.getResource().getName());
      return delta.getResource().getName();
    };
    
    return filter;
  }
}
