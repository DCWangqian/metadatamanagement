/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').service('TexUploadService',
function($translate, Upload, FileResource, JobLoggingService) {
  var uploadTexTemplate = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      Upload.upload({
          url: 'api/data-sets/report',
          fields: {
            'id': dataAcquisitionProjectId
          },
          file: file
            //Upload and document could filled with data successfully
        }).success(function(gridFsFileName) {
          //Download automaticly data filled tex template
          FileResource.download(gridFsFileName).then(function(response) {
            JobLoggingService.success($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.tex.uploadTerminated', {}));
            saveAs(response.data.blob, file.name);
            JobLoggingService.finish($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.tex.saved', {}));
          }).catch(function(error) {
            JobLoggingService.error(error);
            JobLoggingService.cancel($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.tex.cancelled', {}));
          });
          //Server hat issues with the tex file, send error to error output
        }).error(function(error) {
          var endErrorIndex = error.message.indexOf('----');
          var messageShort = error.message.substr(0, endErrorIndex).trim();
          JobLoggingService.error(messageShort);
          JobLoggingService.cancel($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.tex.cancelled', {}));
        });
    }else {
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.tex.cancelled', {}));
    }
  };
  return {
      uploadTexTemplate: uploadTexTemplate
    };
});
