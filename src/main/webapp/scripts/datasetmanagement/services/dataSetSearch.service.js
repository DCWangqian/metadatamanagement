/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchService',
  function(ElasticSearchClient, $q) {
    var createQueryObject = function() {
      return {
        index: 'data_sets',
        type: 'data_sets'
      };
    };

    var findOneById = function(id) {
      var deferred = $q.defer();
      var query =  createQueryObject();
      query.id = id;
      ElasticSearchClient.getSource(query, function(error, response) {
          if (error) {
            deferred.reject(error);
          } else {
            deferred.resolve(response);
          }
        });
      return deferred;
    };
    var findDataSets = function(dataSetIds, selectedAttributes) {
      var ids = _.split(dataSetIds, ',');
      var query =  createQueryObject();
      query.body = {};
      query.body.query = {};
      query.body.query.docs = [];
      _.forEach(ids, function(id) {
        query.body.query.docs.push({
          '_id': id,
          '_source': {
              'include': selectedAttributes
            }
        });
      });
      return ElasticSearchClient.mget(query);
    };
    var findOneByVariableId = function(variableId, selectedAttributes) {
      var query =  createQueryObject();
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
    var findBySurveyId = function(surveyId, selectedAttributes, from,
      size) {
      var query =  createQueryObject();
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
              'surveyIds': surveyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByProjectId = function(dataAcquisitionProjectId, selectedAttributes,
      from, size,
      excludedDataSetId) {
      var query =  createQueryObject();
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
      if (excludedDataSetId) {
        // jscs:disable
        query.body.query.bool.must_not = {
          'term': {
            'id': excludedDataSetId
          }
        };
        // jscs:enable
      }
      return ElasticSearchClient.search(query);
    };
    var findByStudyId = function(studyId, selectedAttributes, from, size) {
      var query =  createQueryObject();
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
              'studyId': studyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var countBy = function(term, value) {
      var query =  createQueryObject();
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
      var mustTerm = {
        'term': {}
      };
      mustTerm.term[term] = value;
      query.body.query.bool.filter.push(mustTerm);
      return ElasticSearchClient.count(query);
    };
    return {
      findOneById: findOneById,
      findOneByVariableId: findOneByVariableId,
      findBySurveyId: findBySurveyId,
      findByProjectId: findByProjectId,
      findByStudyId: findByStudyId,
      findDataSets: findDataSets,
      countBy: countBy
    };
  });
