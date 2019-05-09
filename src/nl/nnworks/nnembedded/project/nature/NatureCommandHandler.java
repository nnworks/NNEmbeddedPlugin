package nl.nnworks.nnembedded.project.nature;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class NatureCommandHandler implements IHandler {

  private static final String NNEMBEDDED_NATURE_ADDNATURE_COMMAND = "nl.nnworks.nnembedded.nature.addnature";
  private static final String NNEMBEDDED_NATURE_REMOVENATURE_COMMAND = "nl.nnworks.nnembedded.nature.removenature";

  @Override
  public void addHandlerListener(IHandlerListener handlerListener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
    if (currentSelection instanceof TreeSelection) {
      TreeSelection selection = (TreeSelection) currentSelection;
      Iterator<?> selectionIterator = selection.iterator();
      while (selectionIterator.hasNext()) {
        Object element = selectionIterator.next();
        if (element instanceof IProject) {
          IProject project = (IProject) element;
          if (event.getCommand().getId().equals(NNEMBEDDED_NATURE_ADDNATURE_COMMAND)) {
            addNature(project, NNEmbeddedProjectNature.NATURE_ID);
          } else if (event.getCommand().getId().equals(NNEMBEDDED_NATURE_REMOVENATURE_COMMAND)) {
            removeNature(project, NNEmbeddedProjectNature.NATURE_ID);
          }
        }
      }
    }

    return null;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isHandled() {
    return true;
  }

  @Override
  public void removeHandlerListener(IHandlerListener handlerListener) {
    // TODO Auto-generated method stub

  }

  private String[] addNature(final IProject project, final String natureId) {
    IProjectDescription description;
    try {
      description = project.getDescription();
      // copy natures array and add the new one
      String[] currentNatures = description.getNatureIds();
      String[] newNatures = new String[currentNatures.length + 1];
      System.arraycopy(currentNatures, 0, newNatures, 0, currentNatures.length);
      newNatures[newNatures.length - 1] = natureId;

      // validate the natures
      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      IStatus status = workspace.validateNatureSet(newNatures);

      // if all is OK, apply the new nature list
      if (status.getCode() == IStatus.OK) {
        description.setNatureIds(newNatures);
        project.setDescription(description, null);
        return newNatures;
      } else {
        // TODO logging
      }
      
      return currentNatures;
    } catch (CoreException e) {
      // TODO logging
    }
    
    return null;
  }

  private String[] removeNature(final IProject project, final String natureIdToRemove) {
    IProjectDescription description;
    try {
      description = project.getDescription();
      // copy natures array and add the new one
      String[] currentNatures = description.getNatureIds();
      String[] newNatures = Arrays.stream(currentNatures).filter((nature) -> { return ! natureIdToRemove.equals(nature); }).collect(Collectors.toList()).toArray(new String[0]); 

      // validate the natures
      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      IStatus status = workspace.validateNatureSet(newNatures);

      // if all is OK, apply the new nature list
      if (status.getCode() == IStatus.OK) {
        description.setNatureIds(newNatures);
        project.setDescription(description, null);
        return newNatures;
      } else {
        // TODO logging
      }
      
      return currentNatures;
    } catch (CoreException e) {
      // TODO logging
    }
    
    return null;
  }
  
}
