/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('QuestionIdBuilderService',
  function() {
    var buildQuestionId = function(instrumentId, questionNumber) {
      return 'que-' + instrumentId + '-' + questionNumber +
        '!';
    };
    return {
      buildQuestionId: buildQuestionId
    };
  });
