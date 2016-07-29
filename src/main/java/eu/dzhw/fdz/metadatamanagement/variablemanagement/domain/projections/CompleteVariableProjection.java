package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Distribution;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterDetails;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.GenerationDetails;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Projection used to expose all attributes (including ids and versions), including sub resources.
 * Spring Data rest does not expose ids and version per default in the json.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Projection(name = "complete", types = Variable.class)
public interface CompleteVariableProjection
    extends AbstractRdcDomainObjectProjection {

  /* Domain Object Attributes */
  I18nString getDataType();

  I18nString getScaleLevel();

  String getName();

  I18nString getLabel();

  I18nString getDescription();

  List<String> getAccessWays();

  List<String> getSameVariablesInPanel();


  /* Nested Objects */
  FilterDetails getFilterDetails();

  GenerationDetails getGenerationDetails();

  Distribution getDistribution();


  /* Foreign Keys */
  List<String> getDataSetIds();

  String atomicQuestionId();

  String getDataAcquisitionProjectId();

  List<String> getSurveyIds();

}
