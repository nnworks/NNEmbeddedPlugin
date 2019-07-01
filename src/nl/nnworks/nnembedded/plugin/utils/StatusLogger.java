package nl.nnworks.nnembedded.plugin.utils;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.statushandlers.StatusManager;

import nl.nnworks.nnembedded.plugin.NNEmEmbeddedPlugin;
import nl.nnworks.nnembedded.plugin.StatusCode;

public class StatusLogger {

  public static void LogStatus(final int severity, final IProject project, final StatusCode code, final String message) {
    LogStatus(severity, project, code, message, null);
  }
  public static void LogStatus(final int severity, final IProject project, final StatusCode code, final String message, final Throwable throwable) {
    OperationStatus operationStatus;
    
    if (project != null) {
      operationStatus = new OperationStatus(severity, NNEmEmbeddedPlugin.PLUGIN_ID, code.getCode(), String.format("[%s] %s", project.getName(), message), throwable);
    } else {
      operationStatus = new OperationStatus(severity, NNEmEmbeddedPlugin.PLUGIN_ID, code.getCode(), String.format("%s", message), throwable);
    }
    
    StatusManager.getManager().handle(operationStatus, StatusManager.LOG);    
  }
  
  public static void LogStatus(final OperationStatus status) {
    StatusManager.getManager().handle(status, StatusManager.LOG);    
  }
  
}
