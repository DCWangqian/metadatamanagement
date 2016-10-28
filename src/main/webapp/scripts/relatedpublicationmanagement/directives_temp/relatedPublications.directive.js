/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedTempPublications',
    function(RelatedPublicationSearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/relatedpublicationmanagement/directives_temp/' +
          'relatedPublications.html.tmpl',
        scope: {},
        controllerAs: 'relatedPublicationController',
        controller: function() {
          var relatedPublicationController = this;
          var blockArea = blockUI.instances.get('blockrelatedPublicationCard');
          relatedPublicationController.page = {
            currentPageNumber: 1,
            totalHits: 0,
            size: 5
          };
          relatedPublicationController.search = function() {
            if (relatedPublicationController.methodParams) {
              blockArea.start();
              if (_.isArray(relatedPublicationController.methodParams)) {
                var searchTerms = _.chunk(relatedPublicationController
                  .methodParams, relatedPublicationController.page.size);
                RelatedPublicationSearchResource[relatedPublicationController
                  .methodName](searchTerms[relatedPublicationController.page
                    .currentPageNumber - 1])
                    .then(function(relatedPublications) {
                      _.pullAllBy(relatedPublications.docs,
                        [{'found': false}], 'found');
                      relatedPublicationController.page.totalHits =
                      relatedPublications.docs
                      .length;
                      relatedPublicationController.relatedPublications =
                      relatedPublications.docs;
                    }).finally(function() {
                      blockArea.stop();
                    });
              } else {
                var from = (relatedPublicationController
                  .page.currentPageNumber - 1) * relatedPublicationController
                  .page.size;
                RelatedPublicationSearchResource[relatedPublicationController
                  .methodName](relatedPublicationController.methodParams, from,
                  relatedPublicationController.page.size)
                  .then(function(relatedPublications) {
                          relatedPublicationController.page.totalHits =
                          relatedPublications.hits.total;
                          relatedPublicationController.relatedPublications =
                          relatedPublications.hits.hits;
                        }).finally(function() {
                          blockArea.stop();
                        });
              }
            }
          };
          relatedPublicationController.search();
        },
        bindToController: {
          methodName: '@',
          methodParams: '='
        }
      };
    });
