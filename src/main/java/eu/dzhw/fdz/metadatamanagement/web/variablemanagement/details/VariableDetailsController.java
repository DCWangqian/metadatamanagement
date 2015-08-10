package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;

/**
 * Show variable search page.
 * 
 * @return variableSearch.html
 */
@Controller
@RequestMapping(path = "/{language}/variables")
public class VariableDetailsController {

  private final VariableService variableService;

  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  @Autowired
  public VariableDetailsController(VariableService variableService) {
    this.variableService = variableService;
  }

  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  @RequestMapping(path = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Callable<VariableResource> get(@PathVariable("id") String id,
      VariableResourceAssembler variableResourceAssembler) {
    return () -> {
      VariableDocument variableDocument = variableService.get(id);
      if (variableDocument != null) {
        return variableResourceAssembler.toResource(variableDocument);
      } else {
        return null;
      }
    };
  }
}
