package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The SearchForm Data transfer object (dto). This dto
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders")
public class VariableSearchFilter extends AbstractSearchFilter {

  /**
   * This is the request parameter of the query. The value is the search query.
   */
  private String query;

  /**
   * This is the request parameter of the scaleLevel filter.
   */
  private String scaleLevel;

  /**
   * This is the request parameter of the surveyTitle filter.
   */
  private String surveyTitle;

  /**
   * This is the request parameter of the dateRange filter.
   */
  @Valid
  private DateRange dateRange;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractQueryDto#getAllFilters()
   */
  @Override
  public Map<Field, String> getAllFilters() {
    Map<Field, String> filterValues = new HashMap<>();

    // ScaleLevel
    if (StringUtils.hasText(this.scaleLevel)) {
      filterValues.put(VariableDocument.SCALE_LEVEL_FIELD, this.scaleLevel);
    }

    // Survey Title
    if (StringUtils.hasText(this.surveyTitle)) {
      filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD, this.surveyTitle);
    }

    //Date Range Start Date value. This value have to be checked by the end field
    //StartDate (Input by User) <= EndDate (Variable)
    if (this.dateRange != null) {
      if (this.dateRange.getStartDate() != null
          && StringUtils.hasText(this.dateRange.getStartDate().toString())) {
        filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE,
            this.dateRange.getStartDate().toString());

      }

      // Date Range End Date This value have to be checked by the start field
      //StartDate (Variable) <= EndDate (Input by User)
      if (this.dateRange.getEndDate() != null
          && StringUtils.hasText(this.dateRange.getEndDate().toString())) {
        filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE,
            this.dateRange.getEndDate().toString());
      }
    }

    return filterValues;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractQueryDto#getFilterFields()
   */
  @Override
  public Map<Field, Integer> getFilterFields() {
    Map<Field, Integer> filterFields = new HashMap<>();

    // ScaleLevel
    filterFields.put(VariableDocument.SCALE_LEVEL_FIELD, VariableSearchFilter.FILTER_TERM);

    // Survey Title
    filterFields.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD,
        VariableSearchFilter.FILTER_TERM);

    // Date Range Start Date
    filterFields.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE,
        VariableSearchFilter.FILTER_RANGE_LTE);

    // Date Range End Date
    filterFields.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE,
        VariableSearchFilter.FILTER_RANGE_GTE);

    return filterFields;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractQueryDto#getAggregationFields()
   */
  @Override
  public Map<Field, Integer> getAggregationFields() {
    Map<Field, Integer> aggregationFields = new HashMap<>();

    // ScaleLevel
    aggregationFields.put(VariableDocument.SCALE_LEVEL_FIELD,
        VariableSearchFilter.AGGREGATION_TERM);

    // Survey Title
    aggregationFields.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD,
        VariableSearchFilter.AGGREGATION_TERM);

    return aggregationFields;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractQueryDto#getQuery()
   */
  @Override
  public String getQuery() {
    return query;
  }

  /* GETTER / SETTER */
  public void setQuery(String query) {
    this.query = query;
  }

  public String getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(String scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public String getSurveyTitle() {
    return surveyTitle;
  }

  public void setSurveyTitle(String surveyTitle) {
    this.surveyTitle = surveyTitle;
  }

  public DateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(DateRange dateRange) {
    this.dateRange = dateRange;
  }
}
