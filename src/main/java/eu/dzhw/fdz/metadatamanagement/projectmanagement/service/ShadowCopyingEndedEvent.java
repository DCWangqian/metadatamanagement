package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.context.ApplicationEvent;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import lombok.Getter;

/**
 * Event emitted if a project release is detected. Contains the release version and projectId of
 * {@link DataAcquisitionProject} as a reference.
 */
@Getter
public class ShadowCopyingEndedEvent extends ApplicationEvent {
  
  private static final long serialVersionUID = 549634157200620526L;

  private String dataAcquisitionProjectId;

  private String previousReleaseVersion;

  private String releaseVersion;

  /**
   * Create a new event instance.
   * 
   * @param source Event emitter reference
   * @param dataAcquisitionProjectId id of the released project
   * @param releaseVersion the version which has been released
   * @param previousReleaseVersion the previous version or null
   */
  public ShadowCopyingEndedEvent(Object source, String dataAcquisitionProjectId,
      String releaseVersion, String previousReleaseVersion) {
    super(source);
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
    this.previousReleaseVersion = previousReleaseVersion;
    this.releaseVersion = releaseVersion;
  }
}
