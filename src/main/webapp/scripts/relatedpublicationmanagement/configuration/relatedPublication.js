'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('relatedPublicationDetail', {
        parent: 'site',
        url: '/related-publications/{id}',
        data: {
          authorities: [],
          pageTitle: 'related-publication-management.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/relatedpublicationmanagement/views/' +
              'relatedPublicationDetail.html.tmpl',
            controller: 'RelatedPublicationDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('relatedPublication.management');
              $translatePartialLoader.addPart('variable.management');
              $translatePartialLoader.addPart('question.management');
              $translatePartialLoader.addPart('survey.management');
              $translatePartialLoader.addPart('dataSet.management');
              $translatePartialLoader.addPart('study.management');
              $translatePartialLoader.addPart('instrument.management');
            }
          ],
          entity: ['$stateParams', 'RelatedPublicationResource',
            function($stateParams, RelatedPublicationResource) {
              return RelatedPublicationResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
