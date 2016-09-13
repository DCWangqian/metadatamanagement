'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function($scope, entity) {
      $scope.study = entity;
      $scope.$watch('study', function() {
        if ($scope.study.$resolved) {
          console.log($scope.study);
        } else {
          console.log(false);
        }
      }, true);
    });
