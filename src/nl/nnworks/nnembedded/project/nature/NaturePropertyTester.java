package nl.nnworks.nnembedded.project.nature;

import java.util.List;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.statushandlers.StatusManager;

public class NaturePropertyTester extends org.eclipse.core.expressions.PropertyTester {

  public static final String CANADDNNEMBEDDEDNATURE_PROERTY = "canAddNNEmbeddedNature";
  public static final String HASNNEMBEDDEDNATURE_PROPERTY = "hasNNEmbeddedNature";
  
  
  public NaturePropertyTester() {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

    if (receiver instanceof List) {
      List<?> receiverList = (List<?>) receiver;
      for (Object obj: receiverList) {
        try {
          if (obj instanceof IProject) {
            IProject project = (IProject) obj; 
            System.out.println(property);
            if (CANADDNNEMBEDDEDNATURE_PROERTY.equals(property)) {
              if (hasCorCppNature(project) && (project.getNature(NNEmbeddedProjectNature.NATURE_ID) == null)) {
                return true;
              }
            } else if (HASNNEMBEDDEDNATURE_PROPERTY.equals(property)) {
              if (project.getNature(NNEmbeddedProjectNature.NATURE_ID) != null) {
                return true;
              }
            }
          }
        } catch (CoreException e) {
          // TODO: fix this
          OperationStatus status = new OperationStatus(1, "blabla", 3, "something went wrong", null);
          StatusManager.getManager().handle(status, StatusManager.LOG);
        }        
      }
    }
    
    return false;
  }
  
  private boolean hasCorCppNature(IProject project) throws CoreException {

    if (project.getNature(NNEmbeddedProjectNature.CNATURE_ID) != null ||
        project.getNature(NNEmbeddedProjectNature.CCNATURE_ID) != null) {
      return true;
    }
    
    return false;
  }
  
}
