/* global document*/

'use strict';
angular.module('metadatamanagementApp').service('VariableSearchDialogService',
  function($mdDialog, blockUI, VariableSearchResource, CleanJSObjectService) {
    var variables = [];
    var showDialog = function() {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'VariableSearchDialogController',
        controllerAs: 'variableSearchDialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          variables: variables
        },
        templateUrl: 'scripts/variablemanagement/' +
          'directives/variableSearchDialog.html.tmpl',
      });
    };
    var findByQuestionId = function(questionId) {
      blockUI.start();
      VariableSearchResource.findByQuestionId(questionId)
        .then(function(items) {
          if (!CleanJSObjectService.isNullOrEmpty(items)) {
            variables = items.hits.hits;
            blockUI.stop();
            showDialog();
          } else {
            blockUI.stop();
          }
        });
    };
    var findVariables = function(variableIds) {
      blockUI.start();
      VariableSearchResource.findVariables(variableIds).then(function(items) {
        if (!CleanJSObjectService.isNullOrEmpty(items)) {
          variables = items.docs;
          blockUI.stop();
          showDialog();
        } else {
          blockUI.stop();
        }
      });
    };
    return {
      findByQuestionId: findByQuestionId,
      findVariables: findVariables
    };
  });
