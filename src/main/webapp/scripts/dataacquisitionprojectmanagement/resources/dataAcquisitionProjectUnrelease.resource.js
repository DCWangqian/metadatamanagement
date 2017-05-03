'use strict';
/*
  Resource url definition for the unrelease process.
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectUnreleaseResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/unrelease', {
      id: '@id'
    }, {
      'unrelease': {
        method: 'POST'
      }
    });
  });
