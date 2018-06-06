package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyAttachmentService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for uploading an study attachment.
 */
@Controller
@RequestMapping("/api")
public class StudyAttachmentResource {

  @Autowired
  private StudyAttachmentService studyAttachmentService;

  /**
   * REST method for for uploading an study attachment.
   * 
   * @param multiPartFile the attachment
   * @param studyAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/studies/attachments", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("studyAttachmentMetadata") 
      @Valid StudyAttachmentMetadata studyAttachmentMetadata)
      throws URISyntaxException, IOException {
    studyAttachmentService.createStudyAttachment(multiPartFile,
        studyAttachmentMetadata);
    return ResponseEntity.created(new URI(UriUtils.encodePath(
        studyAttachmentMetadata.getId(), "UTF-8")))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given study id.
   * 
   * @param studyId The id of an study.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/studies/{studyId}/attachments", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByStudyId(@PathVariable("studyId") String studyId) {
    if (!StringUtils.isEmpty(studyId)) {
      List<StudyAttachmentMetadata> metadata =
          studyAttachmentService.findAllByStudy(studyId);
      return ResponseEntity.ok().cacheControl(CacheControl.noStore())
          .body(metadata);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Delete all attachments of the given study.
   * 
   * @param studyId The id of the study.
   */
  @RequestMapping(path = "/studies/{studyId}/attachments", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllByStudyId(@PathVariable("studyId") String studyId) {
    if (!StringUtils.isEmpty(studyId)) {
      studyAttachmentService.deleteAllByStudyId(studyId);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Delete the given attachments of the given study.
   * 
   * @param studyId The id of the study.
   * 
   */
  @RequestMapping(path = "/studies/{studyId}/attachments/{filename:.+}", 
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> delete(@PathVariable("studyId") String studyId, 
      @PathVariable("filename") String filename) {
    if (!StringUtils.isEmpty(studyId) && !StringUtils.isEmpty(filename)) {
      studyAttachmentService.deleteByStudyIdAndFilename(studyId, filename);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Update the metadata of the given attachment of the given study.
   */
  @RequestMapping(path = "/studies/{studyId}/attachments/{filename:.+}", 
      method = RequestMethod.PUT)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> update(
      @Valid @RequestBody StudyAttachmentMetadata studyAttachmentMetadata) {
    studyAttachmentService.updateAttachmentMetadata(studyAttachmentMetadata);
    return ResponseEntity.noContent().build();    
  }
}
