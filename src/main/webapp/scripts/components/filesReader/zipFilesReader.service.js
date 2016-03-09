/* global XLSX */
'use strict';

angular.module('metadatamanagementApp').service('ZipFilesParser', function($q) {
  this.readAllFileAsync = function(data, dataAcquisitionProjectId) {
    var deferred = $q.defer();
    var jsonContent = {};
    var variablesValues = {};
    var variables = [];
    console.log(data.files);
    Object.keys(data.files).forEach(function(key) {
      var fileName = data.files[key];
      if (!fileName.dir) {
        if (fileName.name.indexOf('.json') > -1) {
          var jsonKey = fileName.name.replace('values/','').replace('.json','');
          variablesValues[jsonKey] = JSON.parse(fileName.asBinary());
        }
        if (fileName.name.indexOf('.xlsx') > -1) {
          var content = XLSX.read(fileName.asBinary(), {type: 'binary'});
          var sheetList = content.SheetNames;
          var worksheet = content.Sheets[sheetList[0]];
          // jscs:disable
          jsonContent = XLSX.utils.sheet_to_json(worksheet);
          // jscs:enable
          for (var i = 0; i < jsonContent.length; i++) {
            var variable = {
              id: jsonContent[i].id,
              accesWays: jsonContent[i].accesWays.replace(/ /g,'').split(','),
              dataAcquisitionProjectId: dataAcquisitionProjectId,
              dataType: {
                en: jsonContent[i]['dataType.en'],
                de: jsonContent[i]['dataTyp.de']
              },
              label: {
                en: jsonContent[i]['label.en'],
                de: jsonContent[i]['label.de']
              },
              description: {
                en: jsonContent[i]['description.en'],
                de: jsonContent[i]['description.de']
              },
              filterDescription: {
                en: jsonContent[i]['filterDescription.en'],
                de: jsonContent[i]['filterDescription.de']
              },
              filterExpression: jsonContent[i].filterExpression,
              filterExpressionLanguage: jsonContent[i].filterExpressionLanguage,
              generationDetails: {
                rule: jsonContent[i]['generationDetails.rule'],
                ruleExpressionLanguage: jsonContent[i]
                ['generationDetails.ruleExpressionLanguage'],
                description: {
                  en: jsonContent[i]['generationDetails.description.en'],
                  de: jsonContent[i]['generationDetails.description.de']
                }
              },
              name: jsonContent[i].name,
              scaleLevel: {
                en: jsonContent[i]['scaleLevel.en'],
                de: jsonContent[i]['scaleLevel.de']
              },
              statistics: {
                firstQuartile: jsonContent[i]['statistics.firstQuartile'],
                highWhisker: jsonContent[i]['statistics.  highWhisker'],
                kurtosis: jsonContent[i]['statistics.kurtosis'],
                lowWhisker: jsonContent[i]['statistics.lowWhisker'],
                maximum: jsonContent[i]['statistics.maximum'],
                meanValue: jsonContent[i]['statistics.meanValue'],
                median: jsonContent[i]['statistics.median'],
                minimum: jsonContent[i]['statistics.minimum'],
                skewness: jsonContent[i]['statistics.skewness'],
                standardDeviation: jsonContent[i]
                ['statistics.standardDeviation'],
                thirdQuartile: jsonContent[i]['statistics.thirdQuartile']
              },
              values: variablesValues[jsonContent[i].id].values,
              surveyId: jsonContent[i].surveyId,
              conceptId: jsonContent[i].conceptId
            };
            variables.push(variable);
          }
        }
      }
    });
    deferred.resolve(variables);
    return deferred.promise;
  };
});
/*sameVariablesInPanel: jsonContent[i].sameVariablesInPanel.
replace(/ /g,'').split(','),*/
