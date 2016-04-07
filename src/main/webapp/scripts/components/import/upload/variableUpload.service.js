/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
function(CustomModal, $translate, ZipReader, VariableBuilder,
  VariableDeleteResource, JobLoggingService) {
  var upload = function(objects) {
    var itemsToUpload = objects.length;
    var j = 0;
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.variable.' +
            'missingId', {
              index: i + 1
            }));
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.uploadTerminated', {}));
        }
      } else {
        objects[i].$save().then(function(data) {
          JobLoggingService.success($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.saved', {
              id: data.id
            }));
          j++;
          if (j === itemsToUpload) {
            JobLoggingService.finish($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.variable.uploadTerminated', {}));
          }
        }).catch(function(error) {
        JobLoggingService.error(error);
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.uploadTerminated', {}));
        }
      });
      }
    }
  };
  var uploadVariables = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      CustomModal.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteVariables', {
              id: dataAcquisitionProjectId
            })).then(function(returnValue) {
              if (returnValue) {
                ZipReader.readZipFileAsync(file)
                   .then(function(files) {
                     var objects = VariableBuilder.getVariables(files,
                       dataAcquisitionProjectId);
                     VariableDeleteResource.deleteByDataAcquisitionProjectId({
                           dataAcquisitionProjectId: dataAcquisitionProjectId},
                          upload(objects), function() {});
                   });
              }else {
                JobLoggingService.cancel('canceld');
              }
            });
    }
  };
  return {
          uploadVariables: uploadVariables
        };
});
