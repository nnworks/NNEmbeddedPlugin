package nl.nnworks.nnembedded.plugin.nature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

import nl.nnworks.nnembedded.plugin.StatusCode;
import nl.nnworks.nnembedded.plugin.project.builder.ProjectConfigBuilder;
import nl.nnworks.nnembedded.plugin.utils.StatusLogger;

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

    System.out.println("check builder");
    if (!hasBuilder(ProjectConfigBuilder.BUILDER_ID)) {
      System.out.println("not present: add builder");
      addBuilder(ProjectConfigBuilder.BUILDER_ID);
    }
  }

  /**
   * Called when this nature is removed from a project
   */
  @Override
  public void deconfigure() throws CoreException {
    System.out.println("deconfigure nature");

    System.out.println("check builder");
    if (hasBuilder(ProjectConfigBuilder.BUILDER_ID)) {
      System.out.println("not present: remove builder");
      removeBuilder(ProjectConfigBuilder.BUILDER_ID);
    }
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

  private void addBuilder(final String builderId) {
    try {
      // add builder to project properties
      final IProjectDescription description = project.getDescription();
      final ICommand buildCommand = description.newCommand();
      buildCommand.setBuilderName(builderId);
   
      final List<ICommand> commands = new ArrayList<ICommand>();
      commands.addAll(Arrays.asList(description.getBuildSpec()));
      commands.add(buildCommand);
   
      description.setBuildSpec(commands.toArray(new ICommand[commands.size()]));
      project.setDescription(description, null);    
    } catch (CoreException e) {
      StatusLogger.LogStatus(IStatus.ERROR, getProject(), StatusCode.ERROR_ADDBUILDER, "Something went wrong while adding builder " + ProjectConfigBuilder.BUILDER_ID, e);
    }
  }
  
  private void removeBuilder(final String builderId) {
    try {
      // add builder to project properties
      final IProjectDescription description = project.getDescription();
      final ICommand buildCommand = description.newCommand();
      buildCommand.setBuilderName(builderId);
   
      final List<ICommand> commands = new ArrayList<ICommand>();
      commands.addAll(Arrays.asList(description.getBuildSpec()));
      for (final ICommand buildSpec : description.getBuildSpec()) {
        if (builderId.equals(buildSpec.getBuilderName())) {
         // remove from list
         commands.remove(buildSpec);
        }
       }
   
      description.setBuildSpec(commands.toArray(new ICommand[commands.size()]));
      project.setDescription(description, null);    
    } catch (CoreException e) {
      StatusLogger.LogStatus(IStatus.ERROR, getProject(), StatusCode.ERROR_REMOVEBUILDER, "Something went wrong while removing builder " + ProjectConfigBuilder.BUILDER_ID, e);
    }
  }
  
  private boolean hasBuilder(final String builderId) {
    try {
      for (final ICommand buildSpec : getProject().getDescription().getBuildSpec()) {
        if (builderId.equals(buildSpec.getBuilderName()))
          return true;
      }
    } catch (final CoreException e) {
      StatusLogger.LogStatus(IStatus.ERROR, getProject(), StatusCode.ERROR_CHECKBUILDER, "Something went wrong while checking presence of builder " + ProjectConfigBuilder.BUILDER_ID, e);
    }

    return false;
  }
}
