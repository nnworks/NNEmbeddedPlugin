package nl.nnworks.nnembedded.plugin.config.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import nl.nnworks.nnembedded.plugin.project.NNEmbeddedProject;

public class PluginPropertiesPage extends PropertyPage implements IWorkbenchPropertyPage {

  public static final String PROPERTYPAGE_ID = "nl.nnworks.nnembedded.plugin.pluginpropertiespage";
  
  private PluginPropertiesComposite composite;

  public PluginPropertiesPage() {

  }

  @Override
  protected Control createContents(final Composite parent) {
    composite = new PluginPropertiesComposite(parent, SWT.NONE, this);
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

  @Override
  public boolean performOk() {
    // save preferences
    NNEmbeddedProject.getNNEmbeddedProject(getProject()).getPreferences().saveProjectPreferences();
    return super.performOk();
  }

  @Override
  public boolean performCancel() {
    NNEmbeddedProject.getNNEmbeddedProject(getProject()).getPreferences().restoreProjectPreferences();
    return super.performCancel();
  }

}
