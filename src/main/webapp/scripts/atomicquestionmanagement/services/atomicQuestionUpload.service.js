/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('AtomicQuestionUploadService',
  function(ExcelReaderService, AtomicQuestionBuilderService,
    AtomicQuestionDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'question-management.log-messages.question.uploadTerminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'question-management.log-messages.question.missingId', {
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
              .getErrorMessages(error, 'atomicQuestion');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadAtomicQuestions = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('atomicQuestion');
      ExcelReaderService.readFileAsync(file).then(function(data) {
        objects = AtomicQuestionBuilderService.getAtomicQuestions(data,
          dataAcquisitionProjectId);
        AtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          },
          upload,
          function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'atomicQuestion');
            errorMessages.forEach(function() {
              JobLoggingService.error(errorMessages.message,
                errorMessages.translationParams);
            });
          });
      }, function() {
        JobLoggingService.cancel(
          'global.log-messages.unsupportedExcelFile', {});
      });
    };
    return {
      uploadAtomicQuestions: uploadAtomicQuestions
    };
  });
