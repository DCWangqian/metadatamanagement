package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatype.PageWithBuckets;

/**
 * A service for searching variables.
 * 
 * @author Amine Limouri
 */
@Service
public class VariableService {

  private final VariableRepository variableRepository;

  /**
   * A constructor.
   */
  @Autowired
  public VariableService(VariableRepository variablesRepository) {
    this.variableRepository = variablesRepository;
  }

  /**
   * Search variables by query. If the query string does not contain text the first n variables are
   * returned as defined in the pageable.
   * 
   * @param query the query for the search in name field.
   * @param scaleLevel a given scalelevel value for filtering the results
   * @param pageable a pageable object.
   * 
   * @return Page of VariableDocuments and Aggregations
   */
  public PageWithBuckets<VariableDocument> search(String query, String scaleLevel,
      Pageable pageable) {

    // in this case of query oder scaleLevel are have text elements
    if (StringUtils.hasText(query) || StringUtils.hasText(scaleLevel)) {
      return variableRepository.matchQueryInAllFieldAndNgrams(query, scaleLevel, pageable);
    }
    // No special case needed for filter like scale level. for no filter and no query -> find all
    return variableRepository.matchAllWithAggregations(pageable);
  }

  /**
   * Load variable by id.
   * 
   * @param id the id for the document.
   * 
   * @return VariableDocument
   */
  public VariableDocument get(String id) {
    return variableRepository.findOne(id);
  }

  /**
   * Saves a variable document to the repository.
   * 
   * @param variableDocument The variableDocument which should be save.
   * @return The saved VariableDocument
   * @see VariableDocument
   */
  public VariableDocument save(VariableDocument variableDocument) {
    return this.variableRepository.save(variableDocument);
  }

  /**
   * Deletes a variable document from the repository by a given id.
   * 
   * @param id The id of the variable document.
   */
  public void delete(String id) {
    this.variableRepository.delete(id);
  }

}
