'use strict';

angular.module('metadatamanagementApp')
  .controller('AccessWaySearchFilterController', [
    '$scope', 'VariableSearchService',
    function($scope, VariableSearchService) {
      // prevent data-set changed events during init
      var initializing = true;
      var init = function() {
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['access-way']) {
          $scope.currentAccessWay =
          $scope.currentSearchParams.filter['access-way'];
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(accessWay) {
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (accessWay) {
          $scope.currentSearchParams.filter['access-way'] = accessWay;
        } else {
          delete $scope.currentSearchParams.filter['access-way'];
        }
        if (!initializing) {
          $scope.accessWayChangedCallback();
        }
        initializing = false;
      };

      $scope.searchAccessWays = function(searchText) {
        return VariableSearchService.findAccessWays(
          searchText, $scope.currentSearchParams.filter);
      };
      init();
    }
  ]);
