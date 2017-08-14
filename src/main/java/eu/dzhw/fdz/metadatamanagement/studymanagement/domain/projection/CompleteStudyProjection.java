package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * Projection used to expose all attributes (including ids and versions). Spring Data rest does not
 * expose ids and version per default in the json.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = Study.class)
public interface CompleteStudyProjection extends AbstractRdcDomainObjectProjection {
  
  I18nString getTitle();
  
  I18nString getDescription();
  
  I18nString getInstitution();
  
  I18nString getSurveySeries();
  
  I18nString getSponsor();
  
  List<Person> getAuthors();
  
  String getDataAcquisitionProjectId();
  
  I18nString getDataAvailability();
  
  I18nString getSurveyDesign();
  
  String getDoi();
  
  I18nString getAnnotations();
}
