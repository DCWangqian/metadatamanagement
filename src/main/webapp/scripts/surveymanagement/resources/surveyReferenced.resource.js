'use strict';

angular.module('metadatamanagementApp').factory('SurveyReferencedResource',
function($resource) {
  return $resource('api/surveys/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/surveys/search/findByIdIn',
      params: {
        projection: 'referenced',
        ids: '@ids'
      }
    }
  });
});
