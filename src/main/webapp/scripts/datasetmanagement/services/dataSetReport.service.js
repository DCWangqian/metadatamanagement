/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').service('DataSetReportService',
  function(Upload, FileResource, JobLoggingService) {
    var uploadTexTemplate = function(file, dataAcquisitionProjectId) {
      if (file !== null) {
        JobLoggingService.start('dataSetReport');
        Upload.upload({
          url: 'api/data-sets/report',
          fields: {
            'id': dataAcquisitionProjectId
          },
          file: file
        }).success(function(gridFsFileName) {
          //Upload and document could filled with data successfully
          //Download automaticly data filled tex template
          FileResource.download(gridFsFileName).then(function(response) {
            JobLoggingService.success(
              'data-set-management.log-messages.tex.upload-terminated', {}
            );
            saveAs(response.data.blob, file.name);
            JobLoggingService.finish(
              'data-set-management.log-messages.tex.saved', {});
          }).catch(function(error) {
            JobLoggingService.error(error);
            JobLoggingService.cancel(
              'data-set-management.log-messages.tex.cancelled', {});
          });
          //Server hat issues with the tex file, send error to error output
        }).error(function(error) {
          var endErrorIndex = error.message.indexOf('----');
          var messageShort = error.message.substr(0, endErrorIndex).trim();
          JobLoggingService.error(messageShort);
          JobLoggingService.cancel(
            'data-set-management.log-messages.tex.cancelled', {});
        });
      } else {
        JobLoggingService.cancel(
          'data-set-management.log-messages.tex.cancelled', {});
      }
    };
    return {
      uploadTexTemplate: uploadTexTemplate
    };
  });
