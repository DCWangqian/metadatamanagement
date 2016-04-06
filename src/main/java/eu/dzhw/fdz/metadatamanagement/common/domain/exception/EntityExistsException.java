package eu.dzhw.fdz.metadatamanagement.common.domain.exception;

import java.util.Arrays;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.CustomParameterizedException;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorConstants;

/**
 * This exception is thrown when the user tries to create an entity in mongo which id is already
 * used.
 * 
 * @author René Reitmann
 */
public class EntityExistsException extends CustomParameterizedException {

  private static final long serialVersionUID = -1068483913940981111L;

  /**
   * Generate the exception.
   * @param entityClass the class of the entity which already exists
   * @param entityId the id of the entity which already exists
   */
  public EntityExistsException(Class<?> entityClass, String entityId) {
    super(ErrorConstants.ERR_ENTITY_EXISTS, entityClass.getSimpleName(), entityId);
  }
  
  public EntityExistsException(Class<?> entityClass, String[] fields) {
    super(ErrorConstants.ERR_ENTITY_COMPOUND_EXISTS, entityClass.getSimpleName(), 
        Arrays.toString(fields));
  }
}