'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDetailController',
    function($mdSidenav, $scope, entity, JobLoggingService,
      DataSetUploadService,
      VariableUploadService, SurveyUploadService, AtomicQuestionUploadService) {
      $scope.dataAcquisitionProject = entity;
      $scope.objLists = {
        dataSetList: {},
        surveyList: {},
        variableList: {},
        atomicQuestionsList:  {}
      };
      $scope.openLeftMenu = function() {
        $mdSidenav('left').toggle();
      };
      $scope.close = function() {
        $mdSidenav('left').close();
      };
      $scope.isOpen = false;
      $scope.count = 0;
      $scope.selectedDirection = 'left';
      $scope.job = JobLoggingService.init();
      $scope.uploadSurveys = function(file) {
        JobLoggingService.start('survey');
        SurveyUploadService
        .uploadSurveys(file, $scope.dataAcquisitionProject.id);
      };
      $scope.uploadAtomicQuestions = function(file) {
        JobLoggingService.start('atomicQuestion');
        AtomicQuestionUploadService
        .uploadAtomicQuestions(file, $scope.dataAcquisitionProject.id);
      };
      $scope.uploadVariables = function(file) {
        JobLoggingService.start('variable');
        VariableUploadService
        .uploadVariables(file, $scope.dataAcquisitionProject.id);
      };
      $scope.uploadDataSets = function(file) {
        JobLoggingService.start('dataSet');
        DataSetUploadService.
        uploadDataSets(file,$scope.dataAcquisitionProject.id);
      };
      $scope.$watch('job.state', function() {
        if ($scope.job.state === 'finished') {
          $scope.$broadcast($scope.job.id +
            '-list-uploaded');
        }
      });
    });
