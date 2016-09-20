'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function($scope, entity, SurveyReferencedResource,
      DataSetReferencedResource, DataSetReportService, blockUI, DialogService,
      ShoppingCartService) {
      $scope.study = entity;
      $scope.cleanedAccessWays = '';

      entity.$promise.then(function(study) {
        $scope.study = study;
        $scope.cleanedAccessWays = '' + $scope.study.accessWays + '"';
        $scope.cleanedAccessWays = $scope.cleanedAccessWays
        .replace(/[\[\]'"]/g, '');
        SurveyReferencedResource.findByDataAcquisitionProjectId(
          {id: $scope.study.id},
          function(surveys) {
            $scope.surveys = surveys._embedded.surveys;
          });
        DataSetReferencedResource.findByDataAcquisitionProjectId(
          {id: $scope.study.id},
          function(dataSets) {
            $scope.dataSets = dataSets._embedded.dataSets;
          });
      });
      $scope.uploadTexTemplate = function(file, dataSetId) {
        DataSetReportService.uploadTexTemplate(file, dataSetId);
      };
      $scope.showQuestions = function() {
        blockUI.start();
        DialogService.showDialog($scope.study.id,
          'question', 'findByDataAcquisitionProjectId');
      };
      $scope.showInstruments = function() {};
      $scope.showRelatedPublication = function() {};
      $scope.addToNotepad = function() {
        ShoppingCartService
        .addToShoppingCart($scope.study.id);
      };
    });
