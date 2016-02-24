/* global d3 */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', ['$scope', 'entity', 'Language',
    '$translate',

    function($scope, entity, Language, $translate) {
      $scope.variable = entity;

      //options for charts
      $scope.optionsRelative = {
        chart: {
          //CSS information
          type: 'discreteBarChart',
          height: 250,
          showValues: true,
          color: ['#0069B4'],
          valueFormat: function(d) {
            //.2f means two numbers after a commata
            return d3.format(',.2f')(d);
          },

          transitionDuration: 500,

          //Point for data extraction to visualisation
          x: function(d) {

            //if en, return en lables
            if (Language.getCurrentInstantly() === 'en') {
              if (d.label.en) {
                return d.label.en;
                //return the code, if label is not set
              } else {
                return d.code;
              }
            }

            //de is default. return de labels.
            if (d.label.de) {
              return d.label.de;
              //return code, if label is not set
            } else {
              return d.code;
            }
          },

          y: function(d) {
            return d.relativeFrequency;
          },
        },

        // title options
        title: {
          enable: true,
          text: '',
        },
        // subtitle options
        subtitle: {
          enable: true,
          text: '',
          css: {
            'text-align': 'center',
            margin: '10px 13px 0px 7px',
          },
        }
      };

      $scope.optionsAbsolute = {
        chart: {
          //CSS information
          type: 'discreteBarChart',
          height: 250,
          showValues: true,
          color: ['#0069B4'],
          valueFormat: function(d) {
            //.2f means two numbers after a commata
            return d3.format(',.2f')(d);
          },

          transitionDuration: 500,

          //Point for data extraction to visualisation
          x: function(d) {

            //if en, return en lables
            if (Language.getCurrentInstantly() === 'en') {
              if (d.label.en) {
                return d.label.en;
                //return the code, if label is not set
              } else {
                return d.code;
              }
            }

            //de is default. return de labels.
            if (d.label.de) {
              return d.label.de;
              //return code, if label is not set
            } else {
              return d.code;
            }
          },

          y: function(d) {
            return d.absoluteFrequency;
          },
        },

        // title options
        title: {
          enable: true,
          text: '',
        },

        // subtitle options
        subtitle: {
          enable: true,
          text: '',
          css: {
            'text-align': 'center',
            margin: '10px 13px 0px 7px',
          },
        }
      };

      //Wait for promise object
      $scope.variable.$promise.then(function() {
        var statistics = $scope.variable.statistics;

        //The data object is for the display
        $scope.data = [{
          values: $scope.variable.values
        }];

        // Box Plot Chart
        //----------------------------------------------------------------------
        $scope.options = {
          chart: {
            type: 'boxPlotChart',
            height: 550,
            color: ['#0069B4'],
            x: function(d) {
              return d.label;
            },
            y: function(d) {
              return d.values.Q3;
            },
            maxBoxWidth: 55,
            yDomain: [statistics.minimum, statistics.maximum]
          }
        };

        // Code here will be linted with JSCS.
        // jscs:disable
        $scope.data2 = [{
          label: $scope.variable.name,
          values: {
            Q1: statistics.firstQuartile,
            Q2: statistics.median,
            Q3: statistics.thirdQuartile,
            whisker_low: statistics.lowWhisker,
            whisker_high: statistics.highWhisker
          }
        }];
        // Code here will be linted with JSCS.
        // jscs:enable

        $translate(
          'metadatamanagementApp.variable.chart.absoluteFrequency').then(
          function(translation) {
            $scope.optionsAbsolute.subtitle.text = translation;
          });

        $translate(
          'metadatamanagementApp.variable.chart.relativeFrequency').then(
          function(translation) {
            $scope.optionsRelative.subtitle.text = translation;
          });
      });
    }
  ]);
