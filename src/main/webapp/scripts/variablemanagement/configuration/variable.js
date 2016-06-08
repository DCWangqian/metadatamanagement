'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('variable', {
        parent: 'site',
        url: '/variables?{page, query}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.variable.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/' +
              'views/variables.html.tmpl',
            controller: 'VariableController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variablesearch');
              $translatePartialLoader.addPart('pagination');
              $translatePartialLoader.addPart('variable');
              $translatePartialLoader.addPart('global');
              return $translate.refresh();
            }
          ]
        },
        reloadOnSearch: false
      })
      .state('variable.new', {
        parent: 'variable',
        url: '/new',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal', function(
          $stateParams,
          $state, $uibModal) {
          $uibModal.open({
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-dialog.html.tmpl',
            controller: 'VariableDialogController',
            size: 'lg',
            resolve: {
              entity: ['VariableResource', function(
                VariableResource) {
                return new VariableResource();
              }],
              isCreateMode: true
            }
          }).result.then(function() {
            $state.go('variable', null, {
              reload: true
            });
          }, function() {
            $state.go('variable');
          });
        }]
      })
      .state('variable.detail', {
        parent: 'site',
        url: '/variables/{id}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.variable.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-detail.html.tmpl',
            controller: 'VariableDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variable');
              return $translate.refresh();
            }
          ],
          entity: ['$stateParams', 'VariableResource',
            function($stateParams, VariableResource) {
              return VariableResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      })
      .state('variable.edit', {
        parent: 'variable',
        url: '/{id}/edit',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal', function(
          $stateParams,
          $state, $uibModal) {
          $uibModal.open({
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-dialog.html.tmpl',
            controller: 'VariableDialogController',
            size: 'lg',
            resolve: {
              entity: ['VariableResource', function(
                VariableResource) {
                return VariableResource.get({
                  id: $stateParams.id
                });
              }],
              isCreateMode: false
            }
          }).result.then(function() {
            $state.go('variable', null, {
              reload: true
            });
          }, function() {
            $state.go('^');
          });
        }]
      });
  });
