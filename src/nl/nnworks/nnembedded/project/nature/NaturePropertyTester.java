package nl.nnworks.nnembedded.project.nature;

import java.util.List;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.statushandlers.StatusManager;

import nl.nnworks.nnembedded.Activator;

public class NaturePropertyTester extends org.eclipse.core.expressions.PropertyTester {

  public static final String CANADDNNEMBEDDEDNATURE_PROERTY = "canAddNNEmbeddedNature";
  public static final String HASNNEMBEDDEDNATURE_PROPERTY = "hasNNEmbeddedNature";

  public NaturePropertyTester() {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

    if (receiver instanceof List) {
      List<?> receiverList = (List<?>) receiver;
      for (Object obj : receiverList) {
        if (obj instanceof IProject) {
          IProject project = (IProject) obj;
          return handleTestForProject(project, property);
        }
      }
    } else if (receiver instanceof IProject) {
      IProject project = (IProject) receiver;
      return handleTestForProject(project, property);
    }

    return false;
  }

  private boolean handleTestForProject(final IProject project, final String property) {
    System.out.println(property);
    try {
      if (CANADDNNEMBEDDEDNATURE_PROERTY.equals(property)) {
        if (hasCorCppNature(project) && (project.getNature(NNEmbeddedProjectNature.NATURE_ID) == null)) {
          return true;
        }
      } else if (HASNNEMBEDDEDNATURE_PROPERTY.equals(property)) {
        if (project.getNature(NNEmbeddedProjectNature.NATURE_ID) != null) {
          return true;
        }
      }
    } catch (CoreException e) {
      OperationStatus status = new OperationStatus(IStatus.ERROR, Activator.PLUGIN_ID, 3, "Something went wrong while checking project natures of project " + project.getName(), e);
      StatusManager.getManager().handle(status, StatusManager.LOG);
    }

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
