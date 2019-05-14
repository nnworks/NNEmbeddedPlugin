package nl.nnworks.nnembedded.plugin.config.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class ProjectPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

  public static final String PROPERTYPAGE_ID = "nl.nnworks.nnembedded.plugin.projectpropertiespage";

  public ProjectPropertyPage() {
    // TODO Auto-generated constructor stub
    
  }

  @Override
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    
    // TODO
    
    return composite;
  }

  public IProject getProject() {
    IProject project = null;
    IAdaptable element = getElement();
    
    if (element instanceof IProject) {
      project = (IProject) element;
    } else if (element != null) {
      project = element.getAdapter(IProject.class);
    }
    return project;
  }

}
