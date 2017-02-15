'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchService',
  function(ElasticSearchClient) {
    var query = {};
    query.type = 'surveys';
    query.index = 'surveys';
    var findSurveys = function(surveyIds, selectedAttributes, from, size) {
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body._source = selectedAttributes;
      query.body.query = {};
      query.body.query.docs = {
        'ids': surveyIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findByProjectId = function(dataAcquisitionProjectId, selectedAttributes,
      from, size, excludedSurveyId) {
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body._source = selectedAttributes;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }]
        }
      };
      if (excludedSurveyId) {
        // jscs:disable
        query.body.query.bool.must_not = {
          'term': {
            'id': excludedSurveyId
          }
        };
        // jscs:enable
      }
      return ElasticSearchClient.search(query);
    };
    var findByVariableId = function(variableId, selectedAttributes) {
      query.body = {};
      query.body.size = 1;
      query.body._source = selectedAttributes;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'variables.id': variableId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var countBy = function(term, value, excludedSurveyId) {
      query.body = {};
      query.body.query = {};
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': []
        }
      };
      var subQuery = {
        'bool': {}
      };
      subQuery.bool.must = [];
      var mustSubQuery = {
        'term': {}
      };
      mustSubQuery.term[term] = value;
      subQuery.bool.must.push(mustSubQuery);
      if (excludedSurveyId) {
        // jscs:disable
        subQuery.bool.must_not = [{
          'term': {
            'id': excludedSurveyId
          }
        }];
        // jscs:enable
      }
      query.body.query.bool.filter.push(subQuery);
      return ElasticSearchClient.count(query);
    };
    return {
      findSurveys: findSurveys,
      findByProjectId: findByProjectId,
      findByVariableId: findByVariableId,
      countBy: countBy
    };
  });
