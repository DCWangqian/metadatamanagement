'use strict';
/* Actually not in use */
angular.module('metadatamanagementApp')
  .factory('DataSetCollectionResource', function($resource) {
    return $resource('/api/data-sets', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
