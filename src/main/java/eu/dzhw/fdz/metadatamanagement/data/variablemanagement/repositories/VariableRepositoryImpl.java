package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;


import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.ZeroTermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This class implements the interface of the custom variable documents repository. This class will
 * be used for the implementation of the repository beans.
 * 
 * @author Daniel Katzberg
 *
 */
public class VariableRepositoryImpl implements VariableRepositoryCustom {

  private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
  public VariableRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate) {
    this.elasticsearchTemplate = elasticsearchTemplate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository.VariablesRepositoryCustom#
   * matchQueryInAllField(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> matchQueryInAllField(String query, Pageable pageable) {
    QueryBuilder queryBuilder =
        matchQuery("_all", query).fuzziness(Fuzziness.AUTO).zeroTermsQuery(ZeroTermsQuery.ALL);

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository.VariablesRepositoryCustom#
   * matchPhrasePrefixQuery(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> matchPhrasePrefixQuery(String query, Pageable pageable) {

    QueryBuilder queryBuilder =
        QueryBuilders.matchPhrasePrefixQuery("_all", query).fuzziness(Fuzziness.AUTO);

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepositoryCustom#
   * matchFilterBySurveyId(java.lang.String, java.lang.String)
   */
  @Override
  public Page<VariableDocument> filterBySurveyIdAndVariableAlias(String surveyIdQuery,
      String variableAliasQuery) {

    QueryBuilder queryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
        FilterBuilders.nestedFilter("variableSurvey",
            FilterBuilders.boolFilter()
                .must(FilterBuilders.termFilter("variableSurvey.surveyId",
                    surveyIdQuery),
            FilterBuilders.termFilter("variableSurvey.variableAlias", variableAliasQuery))));

    SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepositoryCustom#
   * multiMatchQueryOnAllStringFields(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> multiMatchQueryOnAllStringFields(String query, Pageable pageable) {
    QueryBuilder queryBuilder = boolQuery()
        .should(
            multiMatchQuery(query, VariableDocument.DATA_TYPE_FIELD, VariableDocument.LABEL_FIELD,
                VariableDocument.NAME_FIELD, VariableDocument.SCALE_LEVEL_FIELD,
                VariableDocument.ID_FIELD).fuzziness(Fuzziness.AUTO))
        .should(nestedQuery(VariableDocument.VARIABLE_SURVEY_FIELD,
            multiMatchQuery(query, VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD,
                VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD,
                VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD)
                    .fuzziness(Fuzziness.AUTO)))
        .should(nestedQuery(VariableDocument.ANSWER_OPTIONS_FIELD,
            multiMatchQuery(query, VariableDocument.NESTED_ANSWER_OPTIONS_CODE_FIELD,
                VariableDocument.NESTED_ANSWER_OPTIONS_LABEL_FIELD).fuzziness(Fuzziness.AUTO)));

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }
}
