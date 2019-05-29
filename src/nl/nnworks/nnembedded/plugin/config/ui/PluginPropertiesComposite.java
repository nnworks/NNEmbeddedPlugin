package nl.nnworks.nnembedded.plugin.config.ui;

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
import org.eclipse.wb.swt.SWTResourceManager;

public class PluginPropertiesComposite extends Composite {
  private Text projectDescrFile;
  private Composite composite;

  /**
   * Create the composite.
   * @param parent
   * @param style
   */
  public PluginPropertiesComposite(Composite parent, int style) {
    super(parent, style);
    composite = parent;
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
  }

  private class BrowseEventHandler implements Listener {
    
    public BrowseEventHandler() {      
    }
    
    @Override
    public void handleEvent(Event event) {
      FileDialog dialog = new FileDialog(composite.getShell(), SWT.OPEN);
      dialog.setFilterExtensions(new String [] {"*.html"});
      dialog.setFilterPath("c:\\temp");
      dialog.open();      
    }
    
  }
  
  @Override
  protected void checkSubclass() {
    // Disable the check that prevents subclassing of SWT components
  }

}
