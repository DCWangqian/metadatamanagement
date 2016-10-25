'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'variables';
  query.body = {};

  var findVariables = function(variableIds) {
    query.body.query = {};
    query.body.query.docs = {
      'ids': variableIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findBySurveyTitle = function(surveyTitle) {
    query.body.query = {
      'bool': {
        'must': [
          {
            'match_all': {}
          }
        ],
        'filter': [
          {
            'term': {
              'surveyTitles': surveyTitle
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByQuestionId = function(questionId) {
    query.body.query = {
      'bool': {
        'must': [
          {
            'match_all': {}
          }
        ],
        'filter': [
          {
            'term': {
              'questionId': questionId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  return {
    findByQuestionId: findByQuestionId,
    findBySurveyTitle: findBySurveyTitle,
    findVariables: findVariables
  };
});
