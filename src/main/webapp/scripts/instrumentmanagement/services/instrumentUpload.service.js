/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('InstrumentUploadService',
  function(InstrumentBuilderService, InstrumentDeleteResource,
    JobLoggingService, ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService, $rootScope,
    InstrumentAttachmentUploadService, $translate, $mdDialog,
    CleanJSObjectService) {
    //array holding all instrument resources
    var instrumentsToSave;
    //a map instrumentNumber -> (map filename -> (attachment, metadata))
    var attachmentsToUpload;
    var uploadCount;

    var upload = function() {
      if (uploadCount === instrumentsToSave.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          var job = JobLoggingService.getCurrentJob();
          JobLoggingService.finish(
            'instrument-management.log-messages.instrument.upload-terminated', {
              totalInstruments: job.getCounts('instrument').total,
              totalAttachments: job.getCounts('instrument-attachment')
                .total,
              totalErrors: job.errors
            }
          );
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (instrumentsToSave[uploadCount] === null) {
          uploadCount++;
          return upload();
        } else if (!instrumentsToSave[uploadCount].number ||
          instrumentsToSave[uploadCount].number === '') {
          // instrument does not have an id
          var index = uploadCount;
          JobLoggingService.error({
            message: 'instrument-management.log-messages' +
            '.instrument.missing-number',
            messageParams: {
              index: index + 1
            },
            objectType: 'instrument'
          });
          uploadCount++;
          return upload();
        } else {
          instrumentsToSave[uploadCount].$save().then(function() {
            JobLoggingService.success({
              objectType: 'instrument'
            });
            // get the map filename -> {attachment, metadata}
            var attachments = attachmentsToUpload[
              instrumentsToSave[uploadCount].number];
            //upload attachments if there are some
            if (attachments) {
              //upload all attachments sequentially
              var sequentialChain = $q.when();
              var fileNames = Object.keys(attachments);
              fileNames.forEach(function(fileName) {
                sequentialChain = sequentialChain.then(function() {
                  return InstrumentAttachmentUploadService.uploadAttachment(
                    attachments[fileName].attachment,
                    attachments[fileName].metadata).then(
                    function() {
                      JobLoggingService.success({
                        objectType: 'instrument-attachment'
                      });
                    },
                    function(error) {
                      // attachment upload failed
                      var errorMessage =
                        ErrorMessageResolverService
                        .getErrorMessage(error, 'instrument',
                          'instrument-attachment', fileName);
                      JobLoggingService.error({
                        message: errorMessage.message,
                        messageParams: errorMessage.translationParams,
                        subMessages: errorMessage.subMessages,
                        objectType: 'instrument-attachment'
                      });
                    });
                });
              });
              sequentialChain.finally(function() {
                // finished attachment upload => continue with next instrument
                uploadCount++;
                return upload();
              });
            } else {
              //no attachments to upload => continue with next instrument
              uploadCount++;
              return upload();
            }
          }).catch(function(error) {
            // instrument upload failed
            var errorMessage = ErrorMessageResolverService
              .getErrorMessage(error, 'instrument');
            JobLoggingService.error({
              message: errorMessage.message,
              messageParams: errorMessage.translationParams,
              subMessages: errorMessage.subMessages,
              objectType: 'instrument'
            });
            uploadCount++;
            return upload();
          });
        }
      }
    };

    // upload instruments for the given projects
    var uploadInstruments = function(files, dataAcquisitionProjectId) {
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        var confirm = $mdDialog.confirm()
          .title($translate.instant(
            'search-management.delete-messages.delete-instruments-title'))
          .textContent($translate.instant(
            'search-management.delete-messages.delete-instruments', {
              id: dataAcquisitionProjectId
            }))
          .ariaLabel($translate.instant(
            'search-management.delete-messages.delete-instruments', {
              id: dataAcquisitionProjectId
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));
        $mdDialog.show(confirm).then(function() {
          // reset the instrument upload count
          uploadCount = 0;
          // reset the array of instrument resources
          instrumentsToSave = [];
          // reset the map instrumentId ->
          // (map filename -> (attachment, metadata))
          attachmentsToUpload = {};
          JobLoggingService.start('instrument');
          InstrumentDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          }).$promise.then(
            function() {
              // the excel file containing instruments and attachment metadata
              var excelFile;
              // map filename -> file
              var attachmentFiles = {};

              files.forEach(function(file) {
                if (file.name === 'instruments.xlsx') {
                  excelFile = file;
                } else if ((file.path &&
                    file.path.indexOf('attachments') > -1) ||
                  (file.webkitRelativePath &&
                    file.webkitRelativePath.indexOf('attachments') > -1)) {
                  attachmentFiles[file.name] = file;
                }
              });

              if (!excelFile) {
                JobLoggingService.cancel(
                  'global.log-messages.unable-to-read-file', {
                    file: 'instruments.xlsx'
                  });
                return;
              }

              ExcelReaderService.readFileAsync(excelFile, true).then(
                function(excelContent) {
                  var instruments = excelContent.instruments;
                  var attachments = excelContent.attachments;
                  if (instruments) {
                    instruments.forEach(function(instrumentFromExcel) {
                      instrumentsToSave.push(
                        InstrumentBuilderService.buildInstrument(
                          instrumentFromExcel,
                          dataAcquisitionProjectId));
                    });
                  } else {
                    JobLoggingService.cancel(
                      'global.log-messages.unable-to-read-excel-sheet', {
                        sheet: 'instruments'
                      });
                    return $q.reject();
                  }

                  if (attachments) {
                    attachments.forEach(function(metadataFromExcel, index) {
                      if (!metadataFromExcel.instrumentNumber) {
                        JobLoggingService.error({
                          message: 'instrument-management.log-messages' +
                            '.instrument-attachment.missing-instrument-number',
                          messageParams: {
                            index: index + 1
                          },
                          objectType: 'instrument-attachment'
                        });
                        return;
                      }
                      if (!metadataFromExcel.filename) {
                        JobLoggingService.error({
                          message: 'instrument-management.log-messages.' +
                            'instrument-attachment.missing-filename',
                          messageParams: {
                            index: index + 1
                          },
                          objectType: 'instrument-attachment'
                        });
                        return;
                      }
                      if (!attachmentFiles[metadataFromExcel.filename]) {
                        JobLoggingService.error({
                          message: 'instrument-management.log-messages.' +
                            'instrument-attachment.file-not-found',
                          messageParams: {
                            filename: metadataFromExcel.filename
                          },
                          objectType: 'instrument-attachment'
                        });
                        return;
                      }
                      if (!attachmentsToUpload[metadataFromExcel
                        .instrumentNumber]) {
                        attachmentsToUpload[metadataFromExcel
                          .instrumentNumber] = {};
                      }
                      if (!attachmentsToUpload[metadataFromExcel
                        .instrumentNumber]
                        [metadataFromExcel.filename]) {
                        attachmentsToUpload[metadataFromExcel.instrumentNumber]
                          [metadataFromExcel.filename] = {};
                      }
                      attachmentsToUpload[metadataFromExcel.instrumentNumber]
                        [metadataFromExcel.filename].metadata =
                        InstrumentBuilderService
                        .buildInstrumentAttachmentMetadata(
                          metadataFromExcel, dataAcquisitionProjectId);
                      attachmentsToUpload[metadataFromExcel.instrumentNumber]
                        [metadataFromExcel.filename].attachment =
                        attachmentFiles[metadataFromExcel.filename];
                    });
                  } else {
                    JobLoggingService.cancel(
                      'global.log-messages.unable-to-read-excel-sheet', {
                        sheet: 'attachments'
                      });
                    return $q.reject();
                  }
                },
                function() {
                  JobLoggingService.cancel(
                    'global.log-messages.unable-to-read-file', {
                      file: 'instruments.xlsx'
                    });
                  return $q.reject();
                }).then(upload);
            },
            function() {
              JobLoggingService.cancel(
                'instrument-management.log-messages.instrument.unable-to-delete'
              );
              return $q.reject();
            }
          );
        });
      }
    };

    return {
      uploadInstruments: uploadInstruments
    };
  });
