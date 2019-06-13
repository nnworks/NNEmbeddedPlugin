package nl.nnworks.nnembedded.plugin.config.ui;

import java.io.File;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.prefs.BackingStoreException;

import nl.nnworks.nnembedded.plugin.NNEmEmbeddedPlugin;
import nl.nnworks.nnembedded.plugin.nature.NNEmbeddedProjectNature;

public class PluginPropertiesComposite extends Composite {
  private Text projectDescrFile;
  private Composite composite;

  private PluginPropertiesPage propertiesPage;
  
  private String prefLastBrowseDir;
  
  /**
   * Create the composite.
   * @param parent
   * @param style
   */
  public PluginPropertiesComposite(Composite parent, int style, final PluginPropertiesPage propertiesPage) {
    super(parent, style);
    composite = parent;
    this.propertiesPage = propertiesPage;
    
    // retrieve the preferences
    getCurrentPreferences();
    
    setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    setLayout(new GridLayout(5, false));
    
    Label projectDescrLabel = new Label(this, SWT.NONE);
    projectDescrLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    projectDescrLabel.setText("Project description file:");
    new Label(this, SWT.NONE);
    
    FontData defaultFontData = parent.getFont().getFontData()[0];
    
    projectDescrFile = new Text(this, SWT.BORDER);
    GridData gd_projectDescrFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_projectDescrFile.widthHint = 250;
    projectDescrFile.setLayoutData(gd_projectDescrFile);
    projectDescrFile.setFont(SWTResourceManager.getFont(defaultFontData.getName(), defaultFontData.getHeight(), SWT.ITALIC));
    projectDescrFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    projectDescrFile.setToolTipText("Specify the project description file");
    projectDescrFile.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));

    projectDescrFile.setText("default file: nnembedded.pdesc");
    Button browseProjectDescrButton = new Button(this, SWT.NONE);
    browseProjectDescrButton.setToolTipText("locate file...");
    browseProjectDescrButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    browseProjectDescrButton.setText("...");
    new Label(this, SWT.NONE);
    browseProjectDescrButton.addListener(SWT.Selection, new BrowseEventHandler());
    
    getCurrentPreferences();
  }

  private class BrowseEventHandler implements Listener {
    
    public BrowseEventHandler() {      
    }
    
    @Override
    public void handleEvent(Event event) {
      FileDialog dialog = new FileDialog(composite.getShell(), SWT.OPEN);
      dialog.setFilterExtensions(new String [] {"*.pdesc", "*"});
      dialog.setFilterPath(prefLastBrowseDir);
      String selectedPath = dialog.open();
      
      if (selectedPath != null) {
        File selectedFile = new File(selectedPath);
        saveProjectPreference("last-browse-dir", selectedFile.getParent());
      }
    }
    
  }
  
  @Override
  protected void checkSubclass() {
    // Disable the check that prevents subclassing of SWT components
  }

}
