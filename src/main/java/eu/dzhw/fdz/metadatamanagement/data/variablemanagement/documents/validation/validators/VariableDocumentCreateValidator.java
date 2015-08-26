package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;

/**
 * This is the validator for create a variable document.
 * 
 * @author Daniel Katzberg
 *
 */
@Component(value = "VariableDocumentCreateValidator")
public class VariableDocumentCreateValidator extends VariableDocumentValidator {

  @Autowired
  public VariableDocumentCreateValidator(@Qualifier("mvcValidator") Validator jsrValidator,
      DataTypesProvider dataTypesProvider, VariableRepository variableRepository) {
    super(jsrValidator, dataTypesProvider, variableRepository);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.
   * VariableDocumentValidator#getValidationHints()
   */
  @Override
  public Object[] getValidationHints() {
    Object[] validateHints = {Create.class};
    return validateHints;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.
   * VariableDocumentValidator#validateUniqueVariableAlias(eu.dzhw.fdz.metadatamanagement.data.
   * variablemanagement.documents.VariableDocument, org.springframework.validation.Errors)
   */
  @Override
  protected void validateUniqueVariableAlias(VariableDocument variableDocument, Errors errors) {
    {
      // A check for null is not necessary, because it is a type annotation. it can only be checked
      // by
      // initialized objects
      if (variableDocument == null) {
        return;
      }

      if (variableDocument.getVariableSurvey() == null) {
        return;
      }

      // Both are not empty fields -> no valid if there are null
      // but it return valid, because of the NotBlank field. That handles null fields.
      if (variableDocument.getVariableSurvey().getSurveyId() == null
          || variableDocument.getVariableSurvey().getVariableAlias() == null) {
        return;
      }

      // no elements found
      if (this.variableRepository
          .filterBySurveyIdAndVariableAlias(variableDocument.getVariableSurvey().getSurveyId(),
              variableDocument.getVariableSurvey().getVariableAlias())
          .getNumberOfElements() == 0) {
        return;
        // found elements
      } else {
        errors.rejectValue(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD,
            MANDATORY_VARIABLE_SURVEY_VARIABLEALIAS_MESSAGE_CODE);
        return;
      }
    }
  }

}
