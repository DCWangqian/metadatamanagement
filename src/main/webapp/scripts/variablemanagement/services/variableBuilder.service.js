/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
  function(VariableResource, CleanJSObjectService) {
    var buildVariable = function(variableFromExcel, variableFromJson,
      dataAcquisitionProjectId, dataSet, DataSetIdBuilderService) {

      var variableObj = {
        id: dataAcquisitionProjectId + '-' + dataSet +
          '-' + variableFromExcel.name,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        name: variableFromExcel.name,
        annotations: {
          en: variableFromExcel['annotations.en'],
          de: variableFromExcel['annotations.de']
        },
        accessWays: CleanJSObjectService.
        removeWhiteSpace(variableFromExcel.accessWays),
        filterDetails: {
          expression: variableFromExcel['filterDetails.expression'],
          description: {
            de: variableFromExcel['filterDetails.description.de'],
            en: variableFromExcel['filterDetails.description.en']
          },
          expressionLanguage: variableFromExcel[
            'filterDetails.expressionLanguage'
          ]
        },
        generationDetails: {
          rule: variableFromExcel['generationDetails.rule'],
          ruleExpressionLanguage: variableFromExcel[
            'generationDetails.ruleExpressionLanguage'],
          description: {
            en: variableFromExcel['generationDetails.description.en'],
            de: variableFromExcel['generationDetails.description.de']
          }
        },
        panelIdentifier: variableFromExcel.panelIdentifier,
        label: variableFromJson.label,
        dataType: variableFromJson.dataType,
        scaleLevel: variableFromJson.scaleLevel,
        relatedQuestions: [],
        surveyNumbers: variableFromJson.surveyNumbers,
        surveyIds: [],
        indexInDataSet: variableFromJson.indexInDataSet,
        relatedVariables: variableFromJson.relatedVariables,
        distribution: variableFromJson.distribution,
        dataSetId: DataSetIdBuilderService.buildDataSetId(
          dataAcquisitionProjectId, _.split(dataSet, 'ds')[1]),
        dataSetNumber: _.split(dataSet, 'ds')[1]
      };
      _.forEach(variableObj.surveyNumbers, function(number) {
        variableObj.surveyIds
          .push(variableObj.dataAcquisitionProjectId + '-sy' + number);
      });
      _.forEach(variableFromJson.relatedQuestions, function(relatedQuestion) {
        variableObj.relatedQuestions
          .push({
            'instrumentNumber': relatedQuestion.instrumentNumber,
            'instrumentId': dataAcquisitionProjectId +
              '-ins' + relatedQuestion.instrumentNumber,
            'questionNumber': relatedQuestion.questionNumber,
            'questionId': dataAcquisitionProjectId + '-ins' +
              relatedQuestion.instrumentNumber + '-' +
              relatedQuestion.questionNumber,
            relatedQuestionStrings: relatedQuestion.relatedQuestionStrings,
          });
      });
      return new VariableResource(CleanJSObjectService
        .removeEmptyJsonObjects(variableObj));
    };
    return {
      buildVariable: buildVariable
    };
  });
