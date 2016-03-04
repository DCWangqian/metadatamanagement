package eu.dzhw.fdz.metadatamanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;

/**
 * The 'complete' Projection of a atomic question domain object. 'complete' means all attributes
 * will be displayed.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = AtomicQuestion.class)
public interface CompleteAtomicQuestionProjection extends AbstractRdcDomainObjectProjection {

  I18nString getType();

  String getName();

  String getCompositeQuestionName();

  I18nString getFootnote();

  I18nString getQuestionText();

  I18nString getInstruction();

  I18nString getIntroduction();

  I18nString getSectionHeader();

  String getDataAcquisitionProjectId();

  String getQuestionnaireId();

  String getVariableId();

}
