/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('InstrumentBuilderService',
  function(InstrumentResource, CleanJSObjectService,
    InstrumentIdBuilderService, SurveyIdBuilderService) {
    var buildInstrument = function(instrumentFromExcel,
      dataAcquisitionProjectId) {
      var instrument = {
        id: InstrumentIdBuilderService.buildInstrumentId(
          dataAcquisitionProjectId, instrumentFromExcel.number),
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        number: instrumentFromExcel.number,
        title: {
          en: instrumentFromExcel['title.en'],
          de: instrumentFromExcel['title.de']
        },
        description: {
          en: instrumentFromExcel['description.en'],
          de: instrumentFromExcel['description.de']
        },
        type: instrumentFromExcel.type,
        surveyNumbers: (instrumentFromExcel.surveyNumbers + '').split(
          ','),
        surveyIds: []
      };
      _.forEach(instrument.surveyNumbers, function(number) {
        instrument.surveyIds
          .push(SurveyIdBuilderService.buildSurveyId(
            instrument.dataAcquisitionProjectId, number.trim()));
      });
      var cleanedInstrument = CleanJSObjectService
        .removeEmptyJsonObjects(instrument);
      return new InstrumentResource(cleanedInstrument);
    };

    var buildInstrumentAttachmentMetadata = function(metadataFromExcel,
      dataAcquisitionProjectId) {
      var instrumentAttachmentMetadata = {
        instrumentId: dataAcquisitionProjectId + '-ins' +
          metadataFromExcel.instrumentNumber,
        instrumentNumber: metadataFromExcel.instrumentNumber,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        title: {
          en: metadataFromExcel['title.en'],
          de: metadataFromExcel['title.de']
        },
        type: {
          en: metadataFromExcel['type.en'],
          de: metadataFromExcel['type.de']
        },
        fileName: metadataFromExcel.filename
      };
      var cleanedMetadata = CleanJSObjectService
        .removeEmptyJsonObjects(instrumentAttachmentMetadata);
      return cleanedMetadata;
    };
    return {
      buildInstrument: buildInstrument,
      buildInstrumentAttachmentMetadata: buildInstrumentAttachmentMetadata
    };
  });
