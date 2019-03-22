package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Validates the uniqueness of number. Duplicate numbers are permitted for shadow copies.
 */
public class ValidUniqueSurveyNumberValidator
    implements ConstraintValidator<ValidUniqueSurveyNumber, Survey> {

  @Autowired
  private SurveyRepository surveyRepository;

  /*
   * (non-Javadoc)
   *
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidUniqueSurveyNumber constraintAnnotation) {
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Survey survey, ConstraintValidatorContext context) {
    if (survey.isShadow()) {
      return true;
    } else {
      if (survey.getNumber() != null) {
        if (survey.isShadow()) {
          return true;
        } else {
          return isValidMasterSurveyNumber(survey);
        }
      }
      return true;
    }
  }

  private boolean isValidMasterSurveyNumber(Survey survey) {
    List<IdAndVersionProjection> surveys = surveyRepository
        .findIdsByNumberAndDataAcquisitionProjectId(survey
            .getNumber(), survey.getDataAcquisitionProjectId());
    if (surveys.size() > 1) {
      return false;
    }
    if (surveys.size() == 1) {
      return surveys.get(0).getId().equals(survey.getId());
    }
    return true;
  }
}
