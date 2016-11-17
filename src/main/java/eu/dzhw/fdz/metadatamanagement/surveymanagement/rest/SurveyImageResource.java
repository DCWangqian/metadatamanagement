package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyImageService;

/**
 * REST controller for uploading an survey image.
 * 
 * @author Daniel Katzberg
 */
@Controller
@RequestMapping("/api")
public class SurveyImageResource {

  @Inject
  private SurveyImageService surveyImageService;
  
  /**
   * REST method for for uploading an image.
   * @param multiPartFile the image
   * @param id id of image
   * @return response
   * @throws IOException write Exception 
   * @throws URISyntaxException write uri exception
   */
  @RequestMapping(path = "/surveys/images", method = RequestMethod.POST)
  @Timed
  public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile multiPartFile,
      @RequestParam("id") String id) throws IOException, URISyntaxException {
    if (!multiPartFile.isEmpty() && this.surveyImageService
            .checkResponseRateFileName(id, multiPartFile.getOriginalFilename())) {    
      //We need the original file, because we do not know if it is a german or english file.
      //We just know it is a valid name.
      String imageName = this.surveyImageService.saveSurveyImage(multiPartFile.getInputStream(), 
          id, multiPartFile.getOriginalFilename(), multiPartFile.getContentType());
      return ResponseEntity
        .created(new URI("/public/files" + imageName))
        .contentLength(imageName.length())
        .contentType(MediaType.TEXT_PLAIN)
        .body(imageName);      
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}
