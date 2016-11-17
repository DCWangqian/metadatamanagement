/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function($scope, entity, $state,
    SurveySearchDialogService, DataSetSearchDialogService,
    RelatedPublicationSearchDialogService, QuestionSearchDialogService,
    DataSetSearchService, QuestionSearchService,
    RelatedPublicationSearchService, StudySearchService,
    SimpleMessageToastService) {
    $scope.generationCodeToggleFlag = true;
    $scope.notAllRowsVisible = true;
    $scope.counts = {};
    entity.$promise.then(function(variable) {
      $scope.variable = variable;
      StudySearchService
      .findStudy($scope.variable.dataAcquisitionProjectId)
      .then(function(study) {
        if (study.hits.hits.length > 0) {
          $scope.study = study.hits.hits[0]._source;
        }
      });
      if ($scope.variable.questionId) {
        var questionIdAsArray = [];
        questionIdAsArray.push($scope.variable.questionId);
        QuestionSearchService
        .findQuestions(questionIdAsArray)
        .then(function(question) {
          _.pullAllBy(question.docs, [{'found': false}],
          'found');
          if (question.docs.length > 0) {
            $scope.question = question.docs[0]._source;
          }
        });
      }
      DataSetSearchService
      .countBy('variableIds', $scope.variable.id)
      .then(function(dataSetsCount) {
        $scope.counts.dataSetsCount = dataSetsCount.count;
      });
      RelatedPublicationSearchService
      .countBy('variableIds', $scope.variable.id)
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
      if ($scope.counts.surveysCount > 0) {
        var paramObject = {};
        paramObject.methodName = 'findSurveys';
        paramObject.methodParams = $scope.variable.surveyIds;
        SurveySearchDialogService.findSurveys(paramObject);
      }
    };
    $scope.showRelatedDataSets = function() {
      if ($scope.counts.dataSetsCount > 0) {
        var paramObject = {};
        paramObject.methodName = 'findByVariableId';
        paramObject.methodParams = $scope.variable.id;
        DataSetSearchDialogService.findDataSets(paramObject);
      }
    };
    $scope.showRelatedPublications = function() {
      if ($scope.counts.publicationsCount > 0) {
        var paramObject = {};
        paramObject.methodName = 'findByVariableId';
        paramObject.methodParams = $scope.variable.id;
        RelatedPublicationSearchDialogService
        .findRelatedPublications(paramObject);
      }
    };
    $scope.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []
      );
    };
  });
