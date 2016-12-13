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
            JobLoggingService.success({
              message: 'data-set-management.log-messages.tex.upload-terminated'
            });
            saveAs(response.data.blob, file.name);
            console.log('Error Download');
            JobLoggingService.finish(
              'data-set-management.log-messages.tex.saved', {});
          }).catch(function(error) {
            JobLoggingService.error({
              message: error
            });
            JobLoggingService.cancel(
              'data-set-management.log-messages.tex.cancelled', {}
            );
          });
          //Server hat issues with the tex file, send error to error output
        }).error(function(error) {
          console.log('Error Upload');
          console.log(error);
          console.log(error.errors[0].message);
          //TODO DKatzberg
          //var endErrorIndex = error.message.indexOf('----');
          //if no ---- symbol is in the error message
          //if (endErrorIndex <= 0) {
          //  endErrorIndex = error.message.length;
          //}
          //var messageShort = error.message.substr(0, endErrorIndex).trim();
          JobLoggingService.error({
            message: error.errors[0].message,
            messageParams: error.errors[0].invalidValue
          });
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
