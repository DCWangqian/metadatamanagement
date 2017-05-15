'use strict';
angular.module('metadatamanagementApp').controller('FdzWelcomeDialogController',
  function($scope, $mdDialog, localStorageService, bowser) {
    $scope.bowser = bowser;
    $scope.closeDialogForEver = function() {
      localStorageService.set('closeDialogForEver', true);
      $scope.closeDialog();
    };
    $scope.closeDialog = function() {
      $mdDialog.hide();
    };
  });
