'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'related-publication-management': {
        'home': {
          'title': 'Publication'
        },
        'log-messages': {
          'related-publication': {
            'saved': 'Publication with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Publication with RDC-ID {{ id }} has not been saved!',
            'missing-id': 'Publication {{ index }} does not contain a RDC-ID and has not been saved!',
            'duplicate-id': 'The RDC-ID ({{ id }}) of Publication {{ index }} has already been used.',
            'upload-terminated': 'Finished upload of {{ total }} Publications with {{warnings}} warnings and {{ errors }} errors.',
            'unable-to-load-study-serieses': 'Available Study Serieses could not be retrieved from the server!',
            'unable-to-delete': 'Publications could not be deleted!',
            'cancelled': 'Publications upload cancelled!'
          }
        },
        'detail': {
          'label': {
            'publication': 'Publication',
            'publications': 'Publications',
            'doi': 'DOI',
            'sourceReference': 'Source Reference',
            'sourceLink': 'URL',
            'authors': 'Authors',
            'year': 'Year of publication',
            'source-reference': 'Reference',
            'abstract-source': 'Source',
            'studySerieses': 'Study Serieses',
            'annotations': 'Annotations'
          },
          'abstract': 'Abstract',
          'title': '{{ title }} ({{publicationId}})',
          'doi-tooltip': 'Click to open the DOI in a new Tab',
          'sourceLink-tooltip': 'Click to open the source of this publication in a new Tab',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey for which this publication has been written',
              'many': 'Click to show all surveys for which this publication has been written'
            },
            'data-sets': {
              'one': 'Click to show the data set for which this publication has been written',
              'many': 'Click to show all data sets for which this publication has been written'
            },
            'questions': {
              'one': 'Click to show the question for which this publication has been written',
              'many': 'Click to show all questions for which this publication has been written'
            },
            'instruments': {
              'one': 'Click to show the instrument for which this publication has been written',
              'many': 'Click to show all instruments for which this publication has been written'
            },
            'variables': {
              'one': 'Click to show the variable for which this publication has been written',
              'many': 'Click to show all variables for which this publication has been written'
            },
            'studies':{
              'one': 'Click to show the study for which this publication has been written',
              'many': 'Click to show all studies for which this publication has been written'
            },
            'studies-series' : 'Click to show all studies for this study series'
          }
        },
        'assign': {
          'page-title': 'Register Publications for the Study of Project "{{projectId}}"',
          'header': 'Publication Assignment',
          'all-publications-removed-toast': 'All publications have been removed from the study of project "{{id}}"!',
          'empty-publications': 'No Publications have been assigned to the Study yet.',
          'choose-unreleased-project-toast': 'Publications may be assigned or unassigned if and only if the project is currently not released!',
          'search': {
            'placeholder': 'Search Publications and assign to Study',
            'no-publications-found': 'No Publication found'
          },
          'button': {
            'remove-publication': 'Unassign Publication from the Study'
          }
        },
        'error': {
          'related-publication': {
            'one-foreign-key-is-used': 'The Publication has no connection to any other object.',
            'one-study-or-study-series-is-used': 'The publication has no connection to any study or any study series.',
            'valid-related-publication-id': 'The Id of Publication have to be build up after the pattern: "pub-" + {IdFromCitavi} + "$".',
            'study-exists': 'There is no Study with RDC-ID "{{invalidValue}}"!',
            'survey-exists': 'There is no Survey with RDC-ID "{{invalidValue}}"!',
            'dataset-exists': 'There is no Data Set with RDC-ID "{{invalidValue}}"!',
            'variable-exists': 'There is no Variable with RDC-ID "{{invalidValue}}"!',
            'instrument-exists': 'There is no Instrument with RDC-ID "{{invalidValue}}"!',
            'question-exists': 'There is no Question with RDC-ID "{{invalidValue}}"!',
            'study-series-exists': 'There is no Study with Study Series "{{invalidValue.de}}"!',
            'id': {
              'not-empty': 'The RDC-ID of the Publication must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'The RDC-ID must not contain any whitespaces.'
            },
            'source-reference': {
              'not-empty': 'The source reference of the Publication must not be empty!',
              'size': 'The max length of the source reference of the Publication is 2048 signs.'
            },
            'publication-abstract': {
              'size': 'The max length of the publication abstract of the Publication is 1048576 signs.'
            },
            'doi': {
              'size': 'The max length of the DOI of the Publication is 512 signs.'
            },
            'source-link': {
              'pattern': 'The source link of the Publication is not a valid URL.'
            },
            'title': {
              'not-empty': 'The title of the Publication must not be empty!',
              'size': 'The max length of the title of the Publication is 2048 signs.'
            },
            'authors': {
              'size': 'The max length of the authors of the Publication is 2048 signs.',
              'not-empty': 'The authors of the Publication must not be empty!'
            },
            'year': {
              'not-null': 'The Publication Year must not be empty!',
              'valid': 'The Publication Year must be between 1960 and {{currentDate | date :"yyyy"}}.'
            },
            'abstract-source': {
              'i18n-string-size': 'The max length of Source of the Publication is 2048 signs.'
            },
            'language': {
              'not-null': 'The language of the Publication must not be empty!',
              'not-supported': 'The language of the Publication must be a two-letter abbreviation according to ISO 639-1!'
            },
            'annotations': {
              'size': 'The max length of the annotations is 2048 signs.'
            }
          },
          'post-validation': {
            'variable-has-not-a-referenced-study': 'Variable "{{invalidValue}}" belongs to a study which has not been linked to this publication.',
            'survey-has-not-a-referenced-study': 'Survey "{{invalidValue}}" belongs to a study which has not been linked to this publication.',
            'data-set-has-not-a-referenced-study': 'Data Set "{{invalidValue}}" belongs to a study which has not been linked to this publication.',
            'instrument-has-not-a-referenced-study': 'Instrument "{{invalidValue}}" belongs to a study which has not been linked to this publication.',
            'question-has-not-a-referenced-study': 'Question "{{invalidValue}}" belongs to a study which has not been linked to this publication.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
