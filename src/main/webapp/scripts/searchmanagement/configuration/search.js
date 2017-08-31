'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('search', {
      parent: 'site',
      url: '/search?{type}{sort-by}{data-set}' +
      '{instrument}{panel-identifier}{survey}{study}{variable}{question}' +
      '{related-publication}{access-way}{query}',
      reloadOnSearch: false,
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/searchmanagement/views/search.html.tmpl',
          controller: 'SearchController'
        }
      }
    });
  });
