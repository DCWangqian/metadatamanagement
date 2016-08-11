package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * The 'complete' Projection of a question domain object. 'complete' means all attributes will be
 * displayed.
 */
@Projection(name = "complete", types = Question.class)
public interface CompleteQuestionProjection
    extends AbstractRdcDomainObjectProjection {
  
  String getId();
    
  String getDataAcquisitionProjectId();
    
  String getNumber();
    
  I18nString getQuestionText();
    
  I18nString getInstruction();
    
  I18nString getIntroduction();
  
  I18nString getType();
    
  I18nString getAdditionalQuestionText();
  
  ImageType geImageType();
    
  String getTechnicalRepresentation();
    
  List<String> getPredecessor();
    
  List<String> getSuccessor();

  List<String> getVariableIds();

}
