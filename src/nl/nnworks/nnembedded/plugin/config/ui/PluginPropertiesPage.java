package nl.nnworks.nnembedded.plugin.config.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class PluginPropertiesPage extends PropertyPage implements IWorkbenchPropertyPage {

  public static final String PROPERTYPAGE_ID = "nl.nnworks.nnembedded.plugin.pluginpropertiespage";

  public PluginPropertiesPage() {
    // TODO Auto-generated constructor stub
    
  }

  @Override
  protected Control createContents(final Composite parent) {
    Composite composite = new PluginPropertiesComposite(parent, SWT.NONE);
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
