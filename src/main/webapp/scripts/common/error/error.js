'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state(
      'error', {
        parent: 'site',
        url: '/error',
        data: {
          authorities: [],
          pageTitle: 'global.error.title'
        },
        onEnter: function(ToolbarHeaderService) {
          ToolbarHeaderService.updateToolbarHeader({'stateName': 'error'});
        },
        views: {
          'content@': {
            templateUrl: 'scripts/common/error/' +
              'error.html.tmpl'
          }
        }
      });
  });
