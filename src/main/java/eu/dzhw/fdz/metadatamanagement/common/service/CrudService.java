package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SearchDocumentInterface;

/**
 * Service interface for all services doing CRUD operations on {@link AbstractRdcDomainObject}s.
 *
 * @author René Reitmann
 *
 * @param <T> The {@link AbstractRdcDomainObject} which is managed
 */
@Validated
public interface CrudService<T extends AbstractRdcDomainObject> {
  /**
   * Find the {@link SearchDocumentInterface} which corresponds to the
   * {@link AbstractRdcDomainObject}.
   * 
   * @param id The id of the domain object.
   * @return An optional domain object.
   */
  Optional<T> readSearchDocument(String id);
  
  /**
   * Retrieve the {@link AbstractRdcDomainObject} by id.
   * 
   * @param id The id of the {@link AbstractRdcDomainObject}.
   * @return the domain object if found.
   */
  Optional<T> read(String id);

  /**
   * Delete the given domain object.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be deleted.
   */
  void delete(T domainObject);

  /**
   * Validate and save (update or create) the given {@link AbstractRdcDomainObject}.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be saved.
   * @return The saved version.
   */
  T save(@Valid T domainObject);

  /**
   * Validate and create the given {@link AbstractRdcDomainObject}.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be created.
   * @return The created version.
   */
  T create(@Valid T domainObject);
}
