package nl.nnworks.nnembedded.plugin.config.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;

public class PluginPropertiesComposite extends Composite {
  private Text projectDescrFile;

  /**
   * Create the composite.
   * @param parent
   * @param style
   */
  public PluginPropertiesComposite(Composite parent, int style) {
    super(parent, style);
    setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    setLayout(new GridLayout(5, false));
    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);
    
    Label projectDescrLabel = new Label(this, SWT.NONE);
    projectDescrLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    projectDescrLabel.setText("Project description file:");
    new Label(this, SWT.NONE);
    
    projectDescrFile = new Text(this, SWT.BORDER);
    projectDescrFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    GridData gd_projectDescrFile = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
    gd_projectDescrFile.widthHint = 259;
    projectDescrFile.setLayoutData(gd_projectDescrFile);
    projectDescrFile.setToolTipText("Specify the project description file");
    projectDescrFile.setText("test");
    
    Button browseProjectDescrButton = new Button(this, SWT.NONE);
    browseProjectDescrButton.setText("...");

  }

  @Override
  protected void checkSubclass() {
    // Disable the check that prevents subclassing of SWT components
  }

}
