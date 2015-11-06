package eu.dzhw.fdz.metadatamanagement.web.websocket.dto;

/**
 * DTO for storing a user's activity.
 */
public class ActivityDto {

  private String sessionId;

  private String userLogin;

  private String ipAddress;

  private String page;

  private String time;

  /* GETTER / SETTER */
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getUserLogin() {
    return userLogin;
  }

  public void setUserLogin(String userLogin) {
    this.userLogin = userLogin;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ActivityDTO{" + "sessionId='" + sessionId + '\'' + ", userLogin='" + userLogin + '\''
        + ", ipAddress='" + ipAddress + '\'' + ", page='" + page + '\'' + ", time='" + time + '\''
        + '}';
  }
}
