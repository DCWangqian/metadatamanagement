/* global html_beautify */
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function($scope, entity,
    SurveySearchDialogService,
    RelatedPublicationSearchDialogService,
    DataSetSearchService, QuestionSearchService, VariableSearchService,
    RelatedPublicationSearchService, StudySearchService,
    SimpleMessageToastService, PageTitleService, LanguageService,
    CleanJSObjectService) {
    $scope.generationCodeToggleFlag = true;
    $scope.filterDetailsCodeToggleFlag = true;
    $scope.notAllRowsVisible = true;
    $scope.counts = {};
    $scope.variable = entity;
    $scope.validResponsesOrMissingsAvailable = false;
    entity.$promise.then(function() {
      if (!CleanJSObjectService.isNullOrEmpty($scope
          .variable.distribution.missings) || !CleanJSObjectService
        .isNullOrEmpty($scope.variable
          .distribution.validResponses)) {
        $scope.validResponsesOrMissingsAvailable = true;
      }
      var currenLanguage = LanguageService.getCurrentInstantly();
      var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
      PageTitleService.setPageTitle('variable-management.detail.title', {
        label: $scope.variable.label[currenLanguage] ? $scope.variable
          .label[currenLanguage] : $scope.variable.label[
            secondLanguage],
        variableId: $scope.variable.id
      });
      StudySearchService.findStudy($scope.variable.dataAcquisitionProjectId)
        .then(function(study) {
          if (study.hits.hits.length > 0) {
            $scope.study = study.hits.hits[0]._source;
          }
        });
      DataSetSearchService.findDataSets([$scope.variable.dataSetId])
      .then(function(dataSet) {
          if (dataSet.docs.length > 0) {
            $scope.dataSet = dataSet.docs[0]._source;
          }
        });
      QuestionSearchService.countBy('variableIds', $scope.variable.id)
      .then(function(questionsCount) {
        $scope.counts.questionsCount = questionsCount.count;
        if (questionsCount.count === 1) {
          QuestionSearchService
            .findByVariableId($scope.variable.id)
            .then(function(question) {
              $scope.question = question.hits.hits[0]._source;
            });
        }
      });
      /*SurveySearchService
        .countBy('variableIds', $scope.variable.id)
        .then(function(publicationsCount) {
          $scope.counts.publicationsCount = publicationsCount.count;
        });*/

      RelatedPublicationSearchService
        .countBy('variableIds', $scope.variable.id)
        .then(function(publicationsCount) {
          $scope.counts.publicationsCount = publicationsCount.count;
        });

      if ($scope.variable.panelIdentifier) {
        VariableSearchService
          .countBy('panelIdentifier', $scope.variable.panelIdentifier)
          .then(function(sameVariablesInPanel) {
            $scope.counts.sameVariablesInPanel = sameVariablesInPanel.count;
          });
      } else {
        $scope.counts.sameVariablesInPanel = 0;
      }
      $scope.counts.surveysCount = $scope.variable.surveyIds.length;
      if ($scope.variable.filterDetails) {
        html_beautify($scope.variable.filterDetails.expression); //jscs:ignore
      }
      if ($scope.variable.generationDetails) {
        html_beautify($scope.variable.generationDetails.rule); //jscs:ignore
      }
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
    $scope.toggleFilterDetailsCode = function() {
      $scope.filterDetailsCodeToggleFlag = !$scope.filterDetailsCodeToggleFlag;
    };
    $scope.showRelatedSurveys = function() {
      var paramObject = {};
      paramObject.methodName = 'findSurveys';
      paramObject.methodParams = $scope.variable.surveyIds;
      SurveySearchDialogService.findSurveys(paramObject);
    };
    $scope.showRelatedPublications = function() {
      var paramObject = {};
      paramObject.methodName = 'findByVariableId';
      paramObject.methodParams = $scope.variable.id;
      RelatedPublicationSearchDialogService
        .findRelatedPublications(paramObject);
    };
    $scope.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    /* Show headline for Central Tendency,
      if one element is filled with data. */
    $scope.checkCentralTendencyElements = function() {

      return $scope.variable.distribution != null &&
        $scope.variable.distribution.statistics != null &&
        ($scope.variable.distribution.statistics.meanValue != null ||
          $scope.variable.distribution.statistics.median != null ||
          $scope.variable.distribution.statistics.mode != null);
    };

    /* Show headline for Dispersion, if one element is filled with data. */
    $scope.checkDispersionElements = function() {
      return $scope.variable.distribution != null &&
        $scope.variable.distribution.statistics != null &&
        ($scope.variable.distribution.statistics.standardDeviation != null ||
          $scope.variable.distribution.statistics.meanDeviation != null ||
          $scope.variable.distribution.statistics.deviance != null);
    };

    /* Show headline for Distribution, if one element is filled with data. */
    $scope.checkDistributionElements = function() {
      return $scope.variable.distribution != null &&
        $scope.variable.distribution.statistics != null &&
        ($scope.variable.distribution.statistics.skewness != null ||
          $scope.variable.distribution.statistics.kurtosis != null);
    };

  });
