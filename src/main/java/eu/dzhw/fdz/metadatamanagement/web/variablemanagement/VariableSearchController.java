package eu.dzhw.fdz.metadatamanagement.web.variablemanagement;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableResourceAssembler;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableSearchPageResource;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 */
@Controller
@RequestMapping(value = "/{language}/variables/search")
public class VariableSearchController {

  private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private VariableResourceAssembler variableResourceAssembler;
  private PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler;

  /**
   * Autowire needed objects.
   * 
   * @param variableService his is a case with description
   * @param controllerLinkBuilderFactory his is a case with description
   * @param variableResourceAssembler his is a case with description
   * @param pagedResourcesAssembler his is a case with description
   */
  @Autowired
  public VariableSearchController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableResourceAssembler variableResourceAssembler,
      PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.variableResourceAssembler = variableResourceAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  @RequestMapping(method = RequestMethod.GET)
  public Callable<ModelAndView> get(@RequestParam(required = false) String query, Pageable pa) {
    return () -> {
      // Page<VariableDocument> variableDocument = variableService.search(query, pa);
      Page<VariableDocument> variableDocument1 = variableService.searchPhrasePrefix(query, pa);
      PagedResources<VariableResource> pagedVariableResource =
          pagedResourcesAssembler.toResource(variableDocument1, variableResourceAssembler);

      VariableSearchPageResource resource = new VariableSearchPageResource(pagedVariableResource);
      resource.addInternationalizationLinks(VariableSearchController.class,
          controllerLinkBuilderFactory, new Object[] {query, pa});
      ModelAndView modelAndView = new ModelAndView("variables/variableSearch");
      modelAndView.addObject("query", query);
      modelAndView.addObject("resource", resource);

      return modelAndView;
    };
  }
}
