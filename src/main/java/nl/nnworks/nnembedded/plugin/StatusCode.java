package nl.nnworks.nnembedded.plugin;

public enum StatusCode {
  ERROR_ADDNATURE(1),
  ERROR_REMOVENATURE(2),
  ERROR_CHECKNATURES(3),
  ERROR_ADDBUILDER(4),
  ERROR_REMOVEBUILDER(5),
  ERROR_CHECKBUILDER(6),
  ERROR_SAVINGPROPERTIES(7);
  
  
  private int code;
  
  private StatusCode(final int code) {
    this.code = code;
  }
  
  public int getCode() {
    return code;
  }
}
