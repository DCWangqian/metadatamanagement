'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('home', {
        parent: 'site',
        url: '/',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/common/main/main.html.tmpl',
            controller: 'MainController'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate',
            '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('main');
              return $translate.refresh();
            }
          ]
        }
      });
    })
  //Error Handler for non translated angular js elements.
  .config(function($translateProvider) {
    $translateProvider.useMissingTranslationHandler('translationErrorHandler');
  });
