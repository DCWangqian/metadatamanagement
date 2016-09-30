/* global document*/

'use strict';
angular.module('metadatamanagementApp')
.service('SurveySearchDialogService',
  function($mdDialog, blockUI, SurveySearchResource, CleanJSObjectService) {
    var surveys = [];
    var showDialog = function() {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'SurveySearchDialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          surveys: surveys
        },
        templateUrl: 'scripts/surveymanagement/directives/' +
        'surveySearchDialog.html.tmpl',
      });
    };
    var findSurveys = function(surveysIds) {
      blockUI.start();
      SurveySearchResource.findSurveys(surveysIds)
      .then(function(items) {
        if (!CleanJSObjectService.isNullOrEmpty(items)) {
          surveys = items.docs;
          blockUI.stop();
          showDialog();
        } else {
          blockUI.stop();
        }
      });
    };
    return {
      findSurveys: findSurveys
    };
  });
