'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(QuestionSearchService,
      SimpleMessageToastService, id, version, excludes) {
        var loadLatestShadowCopyFallback = function() {
          return QuestionSearchService.findShadowByIdAndVersion(id, null,
            excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'question-management.detail.not-found', {id: id});
              return null;
            }
          });
        };
        return QuestionSearchService.findShadowByIdAndVersion(id, version,
          excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              return loadLatestShadowCopyFallback();
            }
          });
      };
    $urlRouterProvider.when('/de/questions/', '/de/error');
    $urlRouterProvider.when('/en/questions/', '/en/error');
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/questions/{id}?{version}{query}{page}{size}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
        },
        views: {
          'content@': {
            templateUrl: 'scripts/questionmanagement/views/' +
              'question-detail.html.tmpl',
            controller: 'QuestionDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'QuestionSearchService', 'Principal',
          'SimpleMessageToastService', '$q',
          function($stateParams, QuestionSearchService, Principal,
            SimpleMessageToastService, $q) {
            var excludedAttributes = ['nested*', 'dataSets',
                'variables','relatedPublications','concepts'];
            if (Principal.loginName() && !$stateParams.version) {
              return QuestionSearchService.findOneById($stateParams.id, null,
                excludedAttributes);
            } else {
              var deferred = $q.defer();
              loadShadowCopy(QuestionSearchService,
                SimpleMessageToastService, $stateParams.id,
                $stateParams.version, excludedAttributes)
                .then(deferred.resolve, deferred.reject);
              return deferred;
            }
          }]
        }
      });
  });
