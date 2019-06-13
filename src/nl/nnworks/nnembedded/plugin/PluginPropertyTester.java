package nl.nnworks.nnembedded.plugin;

import java.util.List;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.statushandlers.StatusManager;

import nl.nnworks.nnembedded.plugin.config.ui.PluginPropertiesPage;
import nl.nnworks.nnembedded.plugin.nature.NNEmbeddedProjectNature;

public class PluginPropertyTester extends org.eclipse.core.expressions.PropertyTester {

  public static final String CANADDNNEMBEDDEDNATURE_PROERTY = "canAddNNEmbeddedNature";
  public static final String HASNNEMBEDDEDNATURE_PROPERTY = "hasNNEmbeddedNature";
  public static final String ISAPPLICABLEPROPERTYRESOURCE_PROPERTY = "isApplicablePropertyResource";

  public PluginPropertyTester() {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

    if (ISAPPLICABLEPROPERTYRESOURCE_PROPERTY.equals(property)) {
      if (receiver instanceof List) {
        List<?> receiverList = (List<?>) receiver;
        for (Object obj : receiverList) {
            return handleApplicableResourceTest(obj, property, args, expectedValue);
        }
      } else {
        return handleApplicableResourceTest(receiver, property, args, expectedValue);
      }
    } else if (receiver instanceof List) {
      List<?> receiverList = (List<?>) receiver;
      for (Object obj : receiverList) {
        if (obj instanceof IProject) {
          IProject project = (IProject) obj;
          return handleTestForNature(project, property);
        }
      }
    } else if (receiver instanceof IProject) {
      IProject project = (IProject) receiver;
      return handleTestForNature(project, property);
    }

    return false;
  }

  private boolean handleApplicableResourceTest(Object obj, String property, Object[] args, Object expectedValue) {
    if (obj instanceof IProject && PluginPropertiesPage.PROPERTYPAGE_ID.equals(args[0])) {
      System.out.println(property + ": true");
      return true;
    }
    
    System.out.println(property + ": false");
    return false;
  }
  
  private boolean handleTestForNature(final IProject project, final String property) {
    try {
      if (CANADDNNEMBEDDEDNATURE_PROERTY.equals(property)) {
        if (hasCorCppNature(project) && !project.hasNature(NNEmbeddedProjectNature.NATURE_ID)) {
          System.out.println(property + ": true");
          return true;
        }
      } else if (HASNNEMBEDDEDNATURE_PROPERTY.equals(property)) {
        if (project.hasNature(NNEmbeddedProjectNature.NATURE_ID)) {
          System.out.println(property + ": true");
          return true;
        }
      }
    } catch (CoreException e) {
      OperationStatus status = new OperationStatus(IStatus.ERROR, NNEmEmbeddedPlugin.PLUGIN_ID, 3, "Something went wrong while checking project natures of project " + project.getName(), e);
      StatusManager.getManager().handle(status, StatusManager.LOG);
    }

    System.out.println(property + ": false");
    return false;
  }

  private boolean hasCorCppNature(IProject project) throws CoreException {

    if (project.getNature(NNEmbeddedProjectNature.CNATURE_ID) != null
        || project.getNature(NNEmbeddedProjectNature.CCNATURE_ID) != null) {
      return true;
    }

    return false;
  }

}
