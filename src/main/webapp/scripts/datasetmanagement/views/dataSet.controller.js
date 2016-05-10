'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetController', function($scope, $state,
    DataSetCollectionResource,
    DataSetReportService, JobLoggingService) {

    $scope.dataSets = [];
    $scope.job = JobLoggingService.init();
    $scope.page = 1;
    $scope.loadAll = function() {
      DataSetCollectionResource.query({
          page: $scope.page - 1
        },
        function(result) {
          $scope.totalItems = result.page.totalElements;
          $scope.dataSets = result._embedded.dataSets;
        });
    };

    $scope.uploadTexTemplate = function(file, dataSetId) {
      JobLoggingService.start('zip');
      DataSetReportService.uploadTexTemplate(file, dataSetId);
    };

    $scope.loadAll();
  });
