package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetReportService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;

/**
 * This Resource handles the upload of tex templates for the variable report.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping("/api")
public class DataSetsReportResource {

  @Autowired
  private DataSetReportService dataSetReportService;

  /**
   * Accept latex templates under the given request mapping.
   * 
   * @param multiPartFile The latex template as multipart file
   * @param dataSetId the id of the data set, from where the file was uploaded
   * @throws IOException Handles io exception for the template. (Freemarker Templates)
   * @throws TemplateException Handles template exceptions. (Freemarker Templates)
   */
  @PostMapping(value = "/data-sets/report")
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multiPartFile,
      @RequestParam("id") String dataSetId) throws IOException, TemplateException, 
      TemplateIncompleteException {

    // Handles no empty latex templates
    if (!multiPartFile.isEmpty()) {

      // fill the data with data and store the template into mongodb / gridfs
      String fileName = this.dataSetReportService.generateReport(multiPartFile, dataSetId);

      // Return ok. Status 200.
      return ResponseEntity.ok()
        .contentLength(fileName.length())
        .contentType(MediaType.TEXT_PLAIN)
        .body(fileName);

    } else {
      // Return bad request, if file is empty.
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}
