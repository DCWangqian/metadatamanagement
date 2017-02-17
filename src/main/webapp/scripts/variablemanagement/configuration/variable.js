'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/variables/', '/de/error');
    $urlRouterProvider.when('/en/variables/', '/en/error');
    $stateProvider
      .state('variableDetail', {
        parent: 'site',
        url: '/variables/{id}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-detail.html.tmpl',
            controller: 'VariableDetailController'
          }
        },
        resolve: {
          entity: ['$stateParams', 'VariableResource',
            function($stateParams, VariableResource) {
              return VariableResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
