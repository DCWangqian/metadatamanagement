/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
  function(ExcelReaderService, SurveyBuilderService,
    SurveyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'survey-management.log-messages.survey.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'survey-management.log-messages.survey.missing-id', {
              index: index + 1
            });
          uploadCount++;
          return upload();
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success();
            uploadCount++;
            return upload();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'survey');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadSurveys = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      objects = [];
      JobLoggingService.start('survey');
      SurveyDeleteResource.deleteByDataAcquisitionProjectId({
          dataAcquisitionProjectId: dataAcquisitionProjectId
        }).$promise.then(
        function() {
          ExcelReaderService.readFileAsync(file)
          .then(function(surveys) {
              objects = SurveyBuilderService.getSurveys(surveys,
                dataAcquisitionProjectId);
              upload();
            }, function() {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'surveys.xlsx'});
          });
        }, function() {
          JobLoggingService.cancel(
            'survey.log-messages.' +
            'survey.unable-to-delete');
        }
      );
    };
    return {
      uploadSurveys: uploadSurveys
    };
  });
