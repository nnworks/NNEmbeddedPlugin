package nl.nnworks.nnembedded.plugin.config.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import nl.nnworks.nnembedded.plugin.project.NNEmbeddedProject;
import nl.nnworks.nnembedded.plugin.utils.StringConcatCollector;

public class PluginPropertiesComposite extends Composite {
  private static final String UPDATE_CONFIGURATIONS_LABEL = "Update configurations:";
  private static final String BROWSE_BUTTON_TEXT = "...";
  private static final String TOOLTIP_BROWSE_FILE = "locate file...";
  private static final String DEFAULT_PROJECT_DESC_FILE = "default file: nnembedded.pdesc";
  private static final String TOOLTIP_PROJECT_DESCRIPTION_FILE = "Specify the project description file";
  private static final String LABEL_PROJECT_DESCRIPTION_FILE = "Project description file:";
  private static final String[] FILTER = new String[] { "*.pdesc", "*" };
  private static final String BUILD_CONFIGS_SEPARATOR = ",";

  /**
   * preference keys
   */
  private static final String LAST_BROWSE_DIR = "last-browse-dir";
  private static final String FOR_BUILD_CONFIGS = "for-build-configs";
  private static final String FOR_PDESC_FILE = "pdesc-file";

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
    projectDescrFile.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
    projectDescrFile.setText(getPreference(FOR_PDESC_FILE, DEFAULT_PROJECT_DESC_FILE));
    ProjectDescTextHandler handler = new ProjectDescTextHandler();
    projectDescrFile.addModifyListener(handler);
    projectDescrFile.addFocusListener(handler);
    
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
    List<String> buildConfigs = addBuildConfigTableItems(table);
    table.addListener(SWT.Selection, new SelectBuildConfigEventHandler(buildConfigs));
    table.computeSize(10, 10, true);
    new Label(this, SWT.NONE);
  }

  /**
   * Get the preference referred to by key.
   * @return preference referred to by <key>. If not the <defaultValue> is returned.
   */
  private String getPreference(final String key, final String defaultValue) {
    return nnEmbeddedProject.getPreferences().get(key, defaultValue);
  }

  private void setPreference(final String key, final String value) {
    nnEmbeddedProject.getPreferences().put(key, value);
  }

  /**
   * Project description file text box modification listener
   */
  private class ProjectDescTextHandler implements ModifyListener, FocusListener {
    @Override
    public void modifyText(ModifyEvent event) {
      Text projectDescFileText = (Text) event.getSource();
        setPreference(FOR_PDESC_FILE, projectDescFileText.getText());
    }

    @Override
    public void focusGained(FocusEvent event) {
      Text projectDescFileText = (Text) event.getSource();
      if (projectDescFileText.getText().equals(DEFAULT_PROJECT_DESC_FILE)) {
        projectDescFileText.setText("");
      }      
    }

    @Override
    public void focusLost(FocusEvent event) {
      Text projectDescFileText = (Text) event.getSource();
      if (projectDescFileText.getText().length() == 0) {
        projectDescFileText.setText(DEFAULT_PROJECT_DESC_FILE);
      }      
    }
  }

  /**
   * Handler for browsing for the configuration file.
   */
  private class BrowseEventHandler implements Listener {
    public BrowseEventHandler() {
    }

    @Override
    public void handleEvent(Event event) {

      try {
        FileDialog dialog = new FileDialog(composite.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(FILTER);
        dialog.setFilterPath(getPreference(LAST_BROWSE_DIR, System.getProperty("user.home")));
        String selectedPath = dialog.open();
  
        if (selectedPath != null) {
          File selectedFile = new File(selectedPath);
          final String projectPath = propertiesPage.getProject().getLocation().addTrailingSeparator().toString();
          final String pDescPath = selectedFile.getCanonicalPath(); 
          if (pDescPath.startsWith(projectPath)) {
            selectedPath = pDescPath.substring(projectPath.length());
          }
            
          projectDescrFile.setText(selectedPath);
          setPreference(LAST_BROWSE_DIR, selectedFile.getParent());
          setPreference(FOR_PDESC_FILE, selectedPath);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Handler for selecting the build configurations to apply the nnembedded
   * configuration to.
   * 
   * @author pg63ux
   *
   */
  private class SelectBuildConfigEventHandler implements Listener {

    List<String> buildConfigs;

    public SelectBuildConfigEventHandler(final List<String> buildConfigs) {
      this.buildConfigs = buildConfigs;
    }

    @Override
    public void handleEvent(Event event) {
      if (event.detail == SWT.CHECK) {
        TableItem item = (TableItem) event.item;
        if (item.getChecked()) {
          if (!buildConfigs.contains(item.getText())) {
            buildConfigs.add(item.getText());
          }
        } else {
          while (buildConfigs.contains(item.getText())) {
            buildConfigs.remove(item.getText());
          }
        }

        setSelectedBuildConfigurations(buildConfigs);
      }
    }
  }

  @Override
  protected void checkSubclass() {
    // Disable the check that prevents sub-classing of SWT components
  }

  private List<String> addBuildConfigTableItems(final Table table) {

    List<String> selectedBuildConfigs = getSelectedBuildConfigurations();

    for (String configName : getAllBuildConfigurations()) {
      TableItem item = new TableItem(table, SWT.NONE);
      item.setText(configName);
      if (selectedBuildConfigs != null && selectedBuildConfigs.contains(configName)) {
        item.setChecked(true);
      } else if (selectedBuildConfigs == null) {
        item.setChecked(true);
      }
    }

    return selectedBuildConfigs;
  }

  private void setSelectedBuildConfigurations(final List<String> selectedBuildConfigs) {
    String selectedBuildConfigsString = selectedBuildConfigs.stream().collect(StringConcatCollector.collect(BUILD_CONFIGS_SEPARATOR));
    setPreference(FOR_BUILD_CONFIGS, selectedBuildConfigsString);
  }

  private List<String> getSelectedBuildConfigurations() {
    String selectedBuildConfigsPref = getPreference(FOR_BUILD_CONFIGS, null);
    List<String> configs = new ArrayList<String>();
    if (selectedBuildConfigsPref != null) {
      for (String buildConfig : selectedBuildConfigsPref.split(BUILD_CONFIGS_SEPARATOR)) {
        configs.add(buildConfig);
      }
    }

    return configs;
  }

  private String[] getAllBuildConfigurations() {
    IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(propertiesPage.getProject());
    return buildInfo.getConfigurationNames();
  }

}
