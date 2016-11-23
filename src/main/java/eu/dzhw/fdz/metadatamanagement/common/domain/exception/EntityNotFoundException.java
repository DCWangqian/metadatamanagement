package eu.dzhw.fdz.metadatamanagement.common.domain.exception;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.CustomParameterizedException;

/**
 * This exception is thrown when for instance an entity needs to be updated but is not found.
 * 
 * @author René Reitmann
 */
public class EntityNotFoundException extends CustomParameterizedException {

  private static final long serialVersionUID = -1608161733326109527L;

  /**
   * Create the exception with the enities type and its id.
   * @param entityClass The class of the entity which was not found.
   * @param entityId The id of the entity which was not found.
   */
  public EntityNotFoundException(Class<?> entityClass, String entityId) {
    super("entity.notFound.DEMO.String", entityId, null, entityClass.getSimpleName());
  }
}
