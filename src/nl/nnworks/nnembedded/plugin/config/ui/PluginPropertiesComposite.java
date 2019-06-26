package nl.nnworks.nnembedded.plugin.config.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.viewers.TableViewer;
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
import nl.nnworks.nnembedded.plugin.project.NNEmbeddedProject;
import nl.nnworks.nnembedded.plugin.project.ProjectPreferences;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class PluginPropertiesComposite extends Composite {
  private static final String UPDATE_CONFIGURATIONS_LABEL = "Update configurations:";
  private static final String BROWSE_BUTTON_TEXT = "...";
  private static final String TOOLTIP_BROWSE_FILE = "locate file...";
  private static final String DEFAULT_PROJECT_DESC_FILE = "default file: nnembedded.pdesc";
  private static final String TOOLTIP_PROJECT_DESCRIPTION_FILE = "Specify the project description file";
  private static final String LABEL_PROJECT_DESCRIPTION_FILE = "Project description file:";
  private static final String[] FILTER = new String[] { "*.pdesc", "*" };

  /**
   * preference keys  
   */
  private static final String LAST_BROWSE_DIR = "last-browse-dir";
  private static final String FOR_BUILD_CONFIGS = "for-build-configs";
  
  
  private Text projectDescrFile;
  private Composite composite;

  final private PluginPropertiesPage propertiesPage;
  final private NNEmbeddedProject nnEmbeddedProject;

  /**
   * Create the composite.
   * 
   * @param parent
   * @param style
   */
  public PluginPropertiesComposite(final Composite parent, final int style, final PluginPropertiesPage propertiesPage) {
    super(parent, style);
    composite = parent;
    this.propertiesPage = propertiesPage;

    nnEmbeddedProject = NNEmbeddedProject.getNNEmbeddedProject(propertiesPage.getProject());

    setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    setLayout(new GridLayout(3, false));
    
    Label projectDescrLabel = new Label(this, SWT.NONE);
    projectDescrLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    projectDescrLabel.setText(LABEL_PROJECT_DESCRIPTION_FILE);

    FontData defaultFontData = parent.getFont().getFontData()[0];

    projectDescrFile = new Text(this, SWT.BORDER);
    GridData gd_projectDescrFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_projectDescrFile.widthHint = 250;
    projectDescrFile.setLayoutData(gd_projectDescrFile);
    projectDescrFile.setFont(SWTResourceManager.getFont(defaultFontData.getName(), defaultFontData.getHeight(), SWT.ITALIC));
    projectDescrFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    projectDescrFile.setToolTipText(TOOLTIP_PROJECT_DESCRIPTION_FILE);
    projectDescrFile.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));

    projectDescrFile.setText(DEFAULT_PROJECT_DESC_FILE);
    Button browseProjectDescrButton = new Button(this, SWT.NONE);
    browseProjectDescrButton.setToolTipText(TOOLTIP_BROWSE_FILE);
    browseProjectDescrButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    browseProjectDescrButton.setText(BROWSE_BUTTON_TEXT);
    browseProjectDescrButton.addListener(SWT.Selection, new BrowseEventHandler());
    
    Label buildConfigsLabel = new Label(this, SWT.NONE);
    buildConfigsLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
    buildConfigsLabel.setText(UPDATE_CONFIGURATIONS_LABEL);
    
    TableViewer tableViewer = new TableViewer(this, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI);
    Table table = tableViewer.getTable();
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
    addBuildConfigTableItems(table);
    table.addListener(SWT.Selection, new SelectBuildConfigEventHandler());
    table.computeSize(10, 10, true);
    new Label(this, SWT.NONE);
  }

  /**
   * 
   * @return
   */
  private String getPreference(final String key, final String defaultValue) {
    return nnEmbeddedProject.getPreferences().get(key, defaultValue);
  }

  private void setPreference(final String key, final String value) {
    nnEmbeddedProject.getPreferences().put(LAST_BROWSE_DIR, value);
  }

  private class BrowseEventHandler implements Listener {

    public BrowseEventHandler() {
    }

    @Override
    public void handleEvent(Event event) {

      FileDialog dialog = new FileDialog(composite.getShell(), SWT.OPEN);
      dialog.setFilterExtensions(FILTER);
      dialog.setFilterPath(getPreference(LAST_BROWSE_DIR,System.getProperty("user.home")));
      String selectedPath = dialog.open();

      if (selectedPath != null) {
        File selectedFile = new File(selectedPath);
        setPreference(LAST_BROWSE_DIR, selectedFile.getParent());
      }
    }
  }

  private class SelectBuildConfigEventHandler implements Listener {

    public SelectBuildConfigEventHandler() {
    }

    @Override
    public void handleEvent(Event event) {
      if (event.detail == SWT.CHECK) {
        TableItem item = (TableItem)event.item;
        System.out.println(item.getText());
      }
    }
  }
  
  
  @Override
  protected void checkSubclass() {
    // Disable the check that prevents sub-classing of SWT components
  }

  private void addBuildConfigTableItems(final Table table) {
    
    List<String> configs = getSelectedBuildConfigurations();
    
    for (String configName : getAllBuildConfigurations()) {
      TableItem item = new TableItem(table, SWT.NONE);
      item.setText(configName);
      if (configs != null && configs.contains(configName)) {
        item.setChecked(true);
      } else if (configs == null) {
        item.setChecked(true);
      }
    }
  }
  
  private void setSelectedBuildConfigurations(final List selectedBuildConfigs) {
    
  }

  private List<String> getSelectedBuildConfigurations() {
    String forBuildConfigs = getPreference(FOR_BUILD_CONFIGS, null);
    List<String> configs = null;
    if (forBuildConfigs != null) {
      configs = Arrays.asList(forBuildConfigs.split(","));
    }
    
    return configs;
  }
  
  
  private String[] getAllBuildConfigurations() {
    IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(propertiesPage.getProject());
    return buildInfo.getConfigurationNames();
  }
  
}
