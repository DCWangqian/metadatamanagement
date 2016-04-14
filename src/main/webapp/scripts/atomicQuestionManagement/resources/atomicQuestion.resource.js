'use strict';

angular.module('metadatamanagementApp')
  .factory('AtomicQuestion', function($resource) {
    return $resource('api/atomic-questions/:id', {id: '@id'}, {
      'get': {
        method: 'GET',
        params:  {projection: 'complete'}
      },
      'save': {
        method: 'PUT'
      }
    });
  });
