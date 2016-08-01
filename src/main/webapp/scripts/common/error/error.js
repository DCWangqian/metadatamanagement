'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state(
      'error', {
        parent: 'site',
        url: '/error',
        data: {
          authorities: [],
          pageTitle: 'error.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/common/error/' +
              'error.html.tmpl'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('error');
            }
          ]
        }
      }).state(
      'accessdenied', {
        parent: 'site',
        url: '/accessdenied',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/common/error/' +
              'accessdenied.html.tmpl'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('error');
            }
          ]
        }
      });
  });
