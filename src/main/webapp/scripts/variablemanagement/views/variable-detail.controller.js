'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function($scope, entity, $state,
    SurveySearchDialogService, DataSetSearchDialogService,
    RelatedPublicationSearchDialogService, QuestionSearchDialogService,
    DataSetSearchResource, QuestionSearchResource,
    RelatedPublicationSearchResource, StudySearchResource,
    SimpleMessageToastService) {
    $scope.generationCodeToggleFlag = true;
    $scope.notAllRowsVisible = true;
    $scope.counts = {};
    entity.$promise.then(function(variable) {
      $scope.variable = variable;
      StudySearchResource
      .findStudy($scope.variable.dataAcquisitionProjectId)
      .then(function(study) {
        $scope.study = study.hits.hits[0]._source;
      });
      if ($scope.variable.questionId) {
        QuestionSearchResource
        .findQuestion($scope.variable.questionId)
        .then(function(question) {
          $scope.question = question.hits.hits[0]._source;
        });
      }
      DataSetSearchResource
      .getCounts('variableIds', $scope.variable.id)
      .then(function(dataSetsCount) {
        $scope.counts.dataSetsCount = dataSetsCount.count;
      });
      RelatedPublicationSearchResource
      .getCounts('variableIds', $scope.variable.id)
      .then(function(publicationsCount) {
        $scope.counts.publicationsCount = publicationsCount.count;
      });
      $scope.counts.surveysCount =  $scope.variable.surveyIds.length;
    });
    $scope.isRowHidden = function(index) {
      if (index <= 4 || index >= $scope
        .variable.distribution.validResponses.length - 5) {
        return false;
      } else {
        return $scope.notAllRowsVisible;
      }
    };
    $scope.toggleAllRowsVisible = function() {
      $scope.notAllRowsVisible = !$scope.notAllRowsVisible;
    };
    $scope.toggleGenerationCode = function() {
      $scope.generationCodeToggleFlag = !$scope.generationCodeToggleFlag;
    };
    $scope.showRelatedSurveys = function() {
      SurveySearchDialogService.findSurveys('findSurveys',
      $scope.variable.surveyIds, $scope.counts.surveysCount);
    };
    $scope.showRelatedDataSets = function() {
      DataSetSearchDialogService.findDataSets('findByVariableId',
      $scope.variable.id, $scope.counts.dataSetsCount);
    };
    $scope.showRelatedPublications = function() {
      RelatedPublicationSearchDialogService.
      findRelatedPublications('findByVariableId', $scope.variable.id,
      $scope.counts.publicationsCount);
    };
    $scope.openSuccessCopyToClipboardToast = function() {
      SimpleMessageToastService.openSimpleMessageToast(
        'variable-management.log-messages.variable.' +
        'generation-details-rule-success-copy-to-clipboard', []
      );
    };
  });
