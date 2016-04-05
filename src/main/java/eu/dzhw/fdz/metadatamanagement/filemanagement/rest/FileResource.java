package eu.dzhw.fdz.metadatamanagement.filemanagement.rest;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;

/**
 * REST controller for downloading generic files from the GridFS / MongoDB.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping(value = "/public")
public class FileResource {

  @Inject
  private FileService fileService;

  /**
   * Download a file from the GridFS / MongoDB by a given filename.
   */
  @RequestMapping(value = "/files/**")
  public ResponseEntity<?> downloadFile(HttpServletRequest request) {
    String completePath =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String fileName = completePath.replaceFirst("/public/files", "");
    // find file in grid fs / mongo db
    GridFSDBFile gridFsFile = this.fileService.findFile(fileName);

    if (gridFsFile == null) {
      return ResponseEntity.notFound().build();
    }
    
    // Return Status 200 or 304 if not modified
    return ResponseEntity.ok()
      .contentLength(gridFsFile.getLength())
      .cacheControl(CacheControl.maxAge(0, TimeUnit.MILLISECONDS).mustRevalidate())
      .eTag(String.valueOf(gridFsFile.getUploadDate().getTime()))
      .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
      .body(new InputStreamResource(gridFsFile.getInputStream()));
  }

}
