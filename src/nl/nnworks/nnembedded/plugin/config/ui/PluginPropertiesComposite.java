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
import nl.nnworks.nnembedded.plugin.project.ProjectPreferences;
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

  private Text projectDescFile;
  private Composite composite;

  final private PluginPropertiesPage propertiesPage;
  final private NNEmbeddedProject nnEmbeddedProject;

  /**
   * Create the plugin properties composite.
   * 
   * @param parent parent composite of this component
   * @param style composite style
   */
  public PluginPropertiesComposite(final Composite parent, final int style, final PluginPropertiesPage propertiesPage) {
    super(parent, style);
    composite = parent;
    this.propertiesPage = propertiesPage;

    nnEmbeddedProject = NNEmbeddedProject.getNNEmbeddedProject(propertiesPage.getProject());

    setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    setLayout(new GridLayout(3, false));

    Label projectDescLabel = new Label(this, SWT.NONE);
    projectDescLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    projectDescLabel.setText(LABEL_PROJECT_DESCRIPTION_FILE);

    FontData defaultFontData = parent.getFont().getFontData()[0];

    projectDescFile = new Text(this, SWT.BORDER);
    GridData gd_projectDescrFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_projectDescrFile.widthHint = 250;
    projectDescFile.setLayoutData(gd_projectDescrFile);
    projectDescFile.setFont(SWTResourceManager.getFont(defaultFontData.getName(), defaultFontData.getHeight(), SWT.ITALIC));
    projectDescFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    projectDescFile.setToolTipText(TOOLTIP_PROJECT_DESCRIPTION_FILE);
    projectDescFile.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
    projectDescFile.setText(getPreference(ProjectPreferences.FOR_PDESC_FILE, DEFAULT_PROJECT_DESC_FILE));
    ProjectDescTextHandler handler = new ProjectDescTextHandler();
    projectDescFile.addModifyListener(handler);
    projectDescFile.addFocusListener(handler);
    
    Button browseProjectDescButton = new Button(this, SWT.NONE);
    browseProjectDescButton.setToolTipText(TOOLTIP_BROWSE_FILE);
    browseProjectDescButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    browseProjectDescButton.setText(BROWSE_BUTTON_TEXT);
    browseProjectDescButton.addListener(SWT.Selection, new BrowseEventHandler());

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
   * @param key Reference to the preference.
   * @param defaultValue When the key is not found, the defaultValue is returned.
   * @return preference referred to by key. If not the defaultValue is returned.
   */
  private String getPreference(final String key, final String defaultValue) {
    return nnEmbeddedProject.getPreferences().get(key, defaultValue);
  }

  /**
   * Set the preference value on key. The values are not automatically persisted. Therefore a call to ProjectPreferences.saveProjectPreferences() 
   * should be performed. Normally this should be handled by the apply/close buttons of the properties page.
   * @param key Key of the preference value.
   * @param value The value.
   */
  private void setPreference(final String key, final String value) {
    nnEmbeddedProject.getPreferences().put(key, value);
  }

  /**
   * Project description file text box modification listener.
   */
  private class ProjectDescTextHandler implements ModifyListener, FocusListener {
    @Override
    public void modifyText(ModifyEvent event) {
      Text projectDescFileText = (Text) event.getSource();
        setPreference(ProjectPreferences.FOR_PDESC_FILE, projectDescFileText.getText());
    }

    /**
     * if the default text is set, remove it when the focus is gained.
     */
    @Override
    public void focusGained(FocusEvent event) {
      Text projectDescFileText = (Text) event.getSource();
      if (projectDescFileText.getText().equals(DEFAULT_PROJECT_DESC_FILE)) {
        projectDescFileText.setText("");
      }      
    }

    /**
     * If the text field is empty, set the default text again.
     */
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
        dialog.setFilterPath(getPreference(ProjectPreferences.LAST_BROWSE_DIR, System.getProperty("user.home")));
        String selectedPath = dialog.open();
  
        if (selectedPath != null) {
          File selectedFile = new File(selectedPath);
          final String projectPath = propertiesPage.getProject().getLocation().addTrailingSeparator().toString();
          final String pDescPath = selectedFile.getCanonicalPath(); 
          if (pDescPath.startsWith(projectPath)) {
            selectedPath = pDescPath.substring(projectPath.length());
          }
            
          projectDescFile.setText(selectedPath);
          setPreference(ProjectPreferences.LAST_BROWSE_DIR, selectedFile.getParent());
          setPreference(ProjectPreferences.FOR_PDESC_FILE, selectedPath);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Handler for selecting the build configurations to apply the nnembedded
   * configuration to.
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

  /**
   * Add the build configurations present on the project to the given table as check box items.
   * @param table Table to add the check box items to.
   * @return
   */
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

  /**
   * Update the settings in the preference set.
   * @param selectedBuildConfigs List of build configuration names to set as checked.
   */
  private void setSelectedBuildConfigurations(final List<String> selectedBuildConfigs) {
    String selectedBuildConfigsString = selectedBuildConfigs.stream().collect(StringConcatCollector.collect(BUILD_CONFIGS_SEPARATOR));
    setPreference(ProjectPreferences.FOR_BUILD_CONFIGS, selectedBuildConfigsString);
  }

  /**
   * Get selected build configurations from preferences
   * @return List with selected build configuration names.
   */
  private List<String> getSelectedBuildConfigurations() {
    String selectedBuildConfigsPref = getPreference(ProjectPreferences.FOR_BUILD_CONFIGS, null);
    List<String> configs = new ArrayList<String>();
    if (selectedBuildConfigsPref != null) {
      for (String buildConfig : selectedBuildConfigsPref.split(BUILD_CONFIGS_SEPARATOR)) {
        configs.add(buildConfig);
      }
    }

    return configs;
  }

  /**
   * Get all build configurations on the current project
   * @return String array with build configurations.
   */
  private String[] getAllBuildConfigurations() {
    IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(propertiesPage.getProject());
    return buildInfo.getConfigurationNames();
  }

}
