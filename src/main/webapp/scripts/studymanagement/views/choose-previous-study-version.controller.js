/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousStudyVersionController',
    function(StudyVersionsResource, studyId, $scope, $mdDialog,
      LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        studyId: studyId
      };

      $scope.getStudyVersions = function() {
        StudyVersionsResource.get({
          id: studyId,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(studies) {
              $scope.studies = studies;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(study, index) {
        delete study.version;
        $mdDialog.hide({
          study: study,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getStudyVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getStudyVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'study-management' +
            '.edit.choose-previous-version.current-version-tooltip');
        }
      };

      $scope.getStudyVersions();
    });
