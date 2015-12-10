package eu.dzhw.fdz.metadatamanagement.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityNotFoundException;

/**
 * Service Implementation for managing FdzProject.
 */
@Service
public class FdzProjectService {

  private final Logger log = LoggerFactory.getLogger(FdzProjectService.class);

  @Inject
  private FdzProjectRepository fdzProjectRepository;

  /**
   * Create a fdz project in mongo and elasticsearch.
   * 
   * @param fdzProject The fdzProject to create.
   * @return The created fdzProject.
   * @throws EntityExistsException if there is already a fdzproject with the given id
   */
  public FdzProject createFdzProject(FdzProject fdzProject) {
    try {
      fdzProject = fdzProjectRepository.insert(fdzProject);
    } catch (DuplicateKeyException e) {
      throw new EntityExistsException(FdzProject.class, fdzProject.getName());
    }
    //TODO create search index for projects
    //updateSearchIndices(fdzProject);
    return fdzProject;
  }
  
  /**
   * Update an existing variable in mongo and elasticsearch.
   * 
   * @param fdzProject the fdzProject to update.
   * @return The updated fdzProject.
   * @throws EntityNotFoundException if there is no fdzProject with the given id (name)
   */
  public FdzProject updateFdzProject(FdzProject fdzProject) {
    if (!fdzProjectRepository.exists(fdzProject.getName())) {
      throw new EntityNotFoundException(FdzProject.class, fdzProject.getName());
    }
    fdzProject = fdzProjectRepository.save(fdzProject);
    //TODO create search index for projects
    //updateSearchIndices(fdzProject);
    return fdzProject;
  }

  /**
   * get all the fdzProjects.
   * 
   * @return the list of entities
   */
  public Page<FdzProject> findAll(Pageable pageable) {
    log.debug("Request to get all FdzProjects");
    Page<FdzProject> result = fdzProjectRepository.findAll(pageable);
    return result;
  }

  /**
   * get one fdzProject by name.
   * 
   * @return the entity
   */
  public FdzProject findOne(String name) {
    log.debug("Request to get FdzProject : {}", name);
    FdzProject fdzProject = fdzProjectRepository.findOne(name);
    return fdzProject;
  }

  /**
   * delete the fdzProject by name.
   */
  public void delete(String name) {
    log.debug("Request to delete FdzProject : {}", name);
    fdzProjectRepository.delete(name);
  }
}
