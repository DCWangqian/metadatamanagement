/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('DataSetBuilderService',
  function(DataSetResource, CleanJSObjectService, StudyIdBuilderService,
    DataSetIdBuilderService, SurveyIdBuilderService, JobLoggingService) {

    var buildDataSet = function(dataSet,
      dataAcquisitionProjectId) {
      if (!dataSet || !dataAcquisitionProjectId) {
        return null;
      }

      //Create DataSet Object
      var dataSetObj = {
        id: DataSetIdBuilderService.buildDataSetId(
          dataAcquisitionProjectId, dataSet.number),
        studyId: StudyIdBuilderService.buildStudyId(
          dataAcquisitionProjectId),
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        description: {
          en: dataSet['description.en'],
          de: dataSet['description.de']
        },
        number: dataSet.number,
        surveyNumbers: (dataSet.surveyNumbers + '').
        split(','),
        surveyIds: [],
        type: {
          en: dataSet['type.en'],
          de: dataSet['type.de']
        },
        format: {
          en: dataSet['format.en'],
          de: dataSet['format.de']
        },
        subDataSets: dataSet.subDataSets
      };
      _.forEach(dataSetObj.surveyNumbers, function(number) {
        dataSetObj.surveyIds
          .push(SurveyIdBuilderService.buildSurveyId(
            dataAcquisitionProjectId, number.trim()));
      });
      var cleanedDataSetObject = CleanJSObjectService
        .removeEmptyJsonObjects(dataSetObj);
      return new DataSetResource(cleanedDataSetObject);
    };

    //Create SubdataSet
    var buildSubDataSet = function(subDataSet) {
      var subDataSetErrors = [];
      var error;
      if (!Number(subDataSet.numberOfObservations)) {
        error = {
          message: 'data-set-management.log-messages.data-set.sub-' +
            'data-set.number-of-observations-parse-error',
          translationParams: {
            name: subDataSet.name
          }
        };
        subDataSetErrors.push(error);
      }
      if (!Number(subDataSet.numberOfAnalyzedVariables)) {
        error = {
          message: 'data-set-management.log-messages.data-set.sub-' +
            'data-set.number-of-analyzed-variables-parse-error',
          translationParams: {
            name: subDataSet.name
          }
        };
        subDataSetErrors.push(error);
      }

      if (subDataSetErrors.length === 0) {
        var subDataSetObj = {
          name: subDataSet.name,
          description: {
            de: subDataSet['description.de'],
            en: subDataSet['description.en']
          },
          accessWay: subDataSet.accessWay,
          numberOfObservations: parseInt(subDataSet.numberOfObservations),
          dataSetNumber: subDataSet.dataSetNumber
        };
        var cleanedSubDataSetObject = CleanJSObjectService
          .removeEmptyJsonObjects(subDataSetObj);
        return cleanedSubDataSetObject;
      } else {
        throw subDataSetErrors;
      }
    };

    var buildDataSetAttachments =
      function(attachmentsSheet, dataAcquisitionProjectId, filesMap) {
        var notFoundAttachmentsMap = {};
        var attachmentUploadObjects = [];
        attachmentsSheet.forEach(function(attachment) {
          var metadata = attachment;
          if (attachment.fileName) {
            if (filesMap.dataSets
              .attachments[attachment.fileName]) {
              metadata.fileName = attachment.fileName;
              metadata.title = attachment.title;
              metadata.language = attachment.language;
              metadata.dataSetNumber = attachment.dataSetNumber;
              metadata.dataAcquisitionProjectId = dataAcquisitionProjectId;
              metadata.dataSetId =
                DataSetIdBuilderService.buildDataSetId(dataAcquisitionProjectId,
                  attachment.dataSetNumber);
              metadata.description = {
                'de': attachment['description.de'],
                'en': attachment['description.en']
              };
              attachmentUploadObjects.push({
                'metadata': metadata,
                'file': filesMap.dataSets
                .attachments[attachment.fileName]
              });
            } else {
              if (!notFoundAttachmentsMap[attachment.fileName]) {
                JobLoggingService.error({
                  message: 'data-set-management.log-messages' +
                  '.data-set-attachment.file-not-found',
                  messageParams: {
                    filename: attachment.fileName
                  },
                  objectType: 'attachment'
                });
                notFoundAttachmentsMap[attachment.fileName] = true;
              }
            }
          }
        });

        return attachmentUploadObjects;
      };

    return {
      buildDataSet: buildDataSet,
      buildSubDataSet: buildSubDataSet,
      buildDataSetAttachments: buildDataSetAttachments
    };
  });
