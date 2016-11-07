package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;

/**
 * Service for managing attachments for instruments.
 * 
 * @author René Reitmann
 */
@Service
public class InstrumentAttachmentService {

  @Inject
  private GridFsOperations operations;
  
  @Inject
  private MongoTemplate mongoTemplate;
  
  /**
   * Save the attachment for an instrument. 
   * @param inputStream The inputStream of the attachment.
   * @param contentType The contentType of the attachment.
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   */
  public String saveInstrumentAttachment(InputStream inputStream,
      String contentType, InstrumentAttachmentMetadata metadata) {
    GridFSFile gridFsFile = this.operations.store(inputStream, 
        buildFileName(metadata), contentType, metadata);
    gridFsFile.validate();
    return gridFsFile.getFilename();
  }
  
  /**
   * Delete all attachments of the given instrument.
   * @param instrumentId the id of the instrument.
   */
  public void deleteAllByInstrument(String instrumentId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(instrumentId))));
    this.operations.delete(query);
  }
  
  /**
   * Load all metadata objects from gridfs.
   * @param instrumentId The id of the instrument.
   * @return A list of metadata.
   */
  public List<InstrumentAttachmentMetadata> findAllByInstrument(String instrumentId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(instrumentId))));
    return this.operations.find(query).stream().map(gridfsFile -> {
      return mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class, 
          gridfsFile.getMetaData());
    }).collect(Collectors.toList());
  }
  
  private String buildFileName(InstrumentAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getInstrumentId()) + metadata.getFileName(); 
  }
  
  private String buildFileNamePrefix(String instrumentId) {
    return "/instruments/" + instrumentId + "/attachments/";
  }
}
