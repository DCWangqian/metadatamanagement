/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('FileStorage', function($resource) {
    return $resource('api/files/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
      },
      'save': {
        method: 'PUT'
      }
    });
  });
