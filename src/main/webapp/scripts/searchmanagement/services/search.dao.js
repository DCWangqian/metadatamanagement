/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(LanguageService, ElasticSearchClient,
    CleanJSObjectService) {
    var keyMapping = {
      'variables': {
        'data-set': 'dataSetIds',
        'panel-identifier': 'panelIdentifier'
      },
      'surveys': {
        'instrument': 'instrumentIds',
        'study': 'dataAcquisitionProjectId'
      },
      'questions': {
        'instrument': 'instrumentId',
        'variable': 'variableIds'
      },
      'instruments': {
        'survey': 'surveyIds'
      },
      'data_sets': {
        'survey': 'surveyIds'
      }
    };
    return {
      search: function(queryterm, pageNumber, dataAcquisitionProjectId,
        filter, elasticsearchType, pageSize, sortBy, not) {
        var query = {};
        var projectFilter;
        var studiesFilter;
        query.index = 'metadata_' + LanguageService.getCurrentInstantly();
        query.type = elasticsearchType;
        query.body = {};
        if (sortBy && sortBy !== '') {
          var sortCriteria = {};
          sortCriteria[sortBy] = {
            'order': 'asc'
          };
          query.body.sort = [];
          query.body.sort.push(sortCriteria);
        }
        //a query term
        if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
          query.body.query = {
            'bool': {
              'must': [{
                'match': {
                  'allStringsAsNgrams': {
                    'query': queryterm,
                    'type': 'boolean',
                    'operator': 'AND',
                    'minimum_should_match': '100%',
                    'zero_terms_query': 'NONE'
                  }
                }
              }]
            }
          };
          //no query term
        } else {
          query.body.query = {
            'bool': {
              'must': [{
                'match_all': {}
              }],
            }
          };
        }
        if (not && not !== '') {
          query.body.query.bool['must_not'] = { // jshint ignore:line
            'term': {
              'id': not
            }
          };
        }
        //define from
        query.body.from = (pageNumber - 1) * pageSize;
        //define size
        query.body.size = pageSize;
        query.body.query.bool.filter = {};
        query.body.query.bool.filter.bool = {};
        //aggregations if user is on the all tab
        if (CleanJSObjectService.isNullOrEmpty(elasticsearchType)) {
          //define aggregations
          query.body.aggs = {
            'countByType': {
              'terms': {
                'field': '_type'
              }
            }
          };
        }
        // this filter section should be refactored
        // filter by projectId
        if (dataAcquisitionProjectId && CleanJSObjectService
          .isNullOrEmpty(filter)) {
          projectFilter = {
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          };
          studiesFilter = {
            'term': {
              'studyIds': dataAcquisitionProjectId
            }
          };
          query.body.query.bool.filter.bool.should = [];
          query.body.query.bool.filter.bool.should.push(projectFilter);
          query.body.query.bool.filter.bool.should.push(studiesFilter);
        }

        if (!CleanJSObjectService.isNullOrEmpty(filter)) {
          projectFilter = {
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          };
          query.body.query.bool.filter.bool.must = [];
          _.each(filter, function(value, key) {
            var filterKeyValue = {
              'term': {}
            };
            if (elasticsearchType) {
              var subKeyMapping = keyMapping[elasticsearchType];
              key = subKeyMapping[key];
              filterKeyValue.term[key] = value;
              query.body.query.bool.filter.bool.must.push(
                filterKeyValue);
            }
          });
        }
        return ElasticSearchClient.search(query);
      }
    };
  });
