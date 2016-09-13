'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('studyDetail', {
        parent: 'site',
        url: '/studies/{id}',
        data: {
          authorities: [],
          //TODO should be a i18n string
          pageTitle: 'Studie'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/studymanagement/views/' +
              'study-detail.html.tmpl',
            controller: 'StudyDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('study.management');
            }
          ],
          entity: ['$stateParams', 'StudyResource',
            function($stateParams, StudyResource) {
              return StudyResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
