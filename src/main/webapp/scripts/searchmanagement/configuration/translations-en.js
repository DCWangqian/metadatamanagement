'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'delete-messages': {
          'delete-variables-title': 'Replace all Variables?',
          'delete-variables': 'Are you sure you want to replace all data of the Variables within the Data Acquisition Project "{{ id }}"?',
          'delete-surveys-title': 'Replace all Surveys?',
          'delete-surveys': 'Are you sure you want to replace all data of the Surveys within the Data Acquisition Project "{{ id }}"?',
          'delete-questions-title': 'Replace all Questions?',
          'delete-questions': 'Are you sure you want to replace all Questions within the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-data-sets-title': 'Replace all Data Sets?',
          'delete-data-sets': 'Are you sure you want to replace all Data Sets within the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-studies-title': 'Replace Study?',
          'delete-studies': 'Are you sure you want to replace the Study of the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-related-publications-title': 'Replace all Publications?',
          'delete-related-publications': 'Are you sure you want to replace all Publications?',
          'delete-instruments-title': 'Replace all Instruments?',
          'delete-instruments': 'Are you sure you want to replace all Instruments within the Data Acquisition Project with RDC-ID "{{ id }}"?'
        },
        'detail': {
          'search': 'Search'
        },
        'buttons': {
          'refresh-tooltip': 'Click to refresh the search results',
          'upload-variables-tooltip': 'Click to upload variables for the selected data acquisition project',
          'upload-surveys-tooltip': 'Click to upload surveys for the selected data acquisition project',
          'upload-data-sets-tooltip': 'Click to upload data sets for the selected data acquisition project',
          'upload-questions-tooltip': 'Click to upload questions for the selected data acquisition project',
          'upload-studies-tooltip': 'Click to upload the study with attachments',
          'upload-or-create-studies-tooltip': 'Upload or manually create the study',
          'upload-or-create-surveys-tooltip': 'Upload all surveys or manually create one survey',
          'upload-or-edit-studies-tooltip': 'Click to upload or manually edit the study',
          'create-study-tooltip': 'Click to manually create the study',
          'create-survey-tooltip': 'Click to manually create a new survey',
          'edit-study-tooltip': 'Click to manually edit the study',
          'edit-survey-tooltip': 'Click to manually edit the survey',
          'delete-survey-tooltip': 'Click to delete the survey',
          'upload-related-publications-tooltip': 'Click to upload publications',
          'upload-instruments-tooltip': 'Click to upload instruments for the selected data acquisition project',
          'previous-search-result-tooltip': 'Click (or CTRL+"\u21E6") to show search result {{ index }} ({{ id }})',
          'next-search-result-tooltip': 'Click (or CTRL+"\u21E8") to show search result {{ index }} ({{ id }})'
        },
        'input-label': {
          'all': 'Search for Studies, Variables, Questions, Surveys, Data Sets, Instruments and Publications...',
          'variables': 'Search for Variables...',
          'questions': 'Search for Questions...',
          'surveys': 'Search for Surveys...',
          'data-sets': 'Search for Data Sets...',
          'studies': 'Search for Studies...',
          'related-publications': 'Search for Publications...',
          'instruments': 'Search for Instruments...'
        },
        'no-results-text': {
          'all': 'No results found for your search request.',
          'variables': 'No Variables found for your search request.',
          'questions': 'No Questions found for your search request.',
          'surveys': 'No Surveys found for your search request.',
          'data-sets': 'No Data Sets found for your search request.',
          'studies': 'No Studies found for your search request.',
          'related-publications': 'No Publications found for your search request.',
          'instruments': 'No Instruments found for your search request.'
        },
        'tabs': {
          'variables': 'Variables',
          'variables-tooltip': 'Click to search for variables',
          'questions': 'Questions',
          'questions-tooltip': 'Click to search for questions',
          'surveys': 'Surveys',
          'surveys-tooltip': 'Click to search for surveys',
          'data_sets': 'Data Sets',
          'data_sets-tooltip': 'Click to search for data sets',
          'studies': 'Studies',
          'studies-tooltip': 'Click to search for studies',
          'all': 'All',
          'all-tooltip': 'Click to search for all objects',
          'related_publications': 'Publications',
          'related_publications-tooltip': 'Click to search for publications',
          'instruments': 'Instruments',
          'instruments-tooltip': 'Click to search for instruments'
        },
        'cards': {
          'question-tooltip': 'Click to show question "{{id}}"',
          'variable-tooltip': 'Click to show variable "{{id}}"',
          'data-set-tooltip': 'Click to show data set "{{id}}"',
          'instrument-tooltip': 'Click to show instrument "{{id}}"',
          'survey-tooltip': 'Click to show survey "{{id}}"',
          'study-tooltip': 'Click to show study "{{id}}"',
          'publication-tooltip': 'Click to show publication "{{id}}"'
        },
        'filter': {
          'study': 'Study',
          'data-set': 'Data Set',
          'question': 'Question',
          'related-publication': 'Publication',
          'panel-identifier': 'Panel Identifier',
          'derived-variables-identifier': 'Derived Variables',
          'access-way': 'Access Way',
          'instrument': 'Instrument',
          'variable': 'Variable',
          'survey': 'Survey',
          'study-series': 'Study Series',
          'study-series-de': 'Study Series',
          'study-series-en': 'Study Series',
          'floating-label': {
            'survey': 'Which survey do you want to filter?',
            'instrument': 'Which instrument do you want to filter?',
            'study': 'Which study do you want to filter?',
            'data-set': 'Which data set do you want to filter?',
            'related-publication': 'Which publication do you want to filter?',
            'panel-identifier': 'Which panel identifier of the variables do you want to filter?',
            'derived-variables-identifier': 'Which derived variables do you want to filter?',
            'access-way': 'Which access way do you want to filter?',
            'variable': 'Which variable do you want to filter?',
            'question': 'Which question do you want to filter?',
            'study-series': 'Which study series do you want to filter?',
          },
          'input-label': {
            'studies': 'Select filters for Study Search...',
            'surveys': 'Select filters for Survey Search...',
            'instruments': 'Select filters for Instrument Search...',
            'questions': 'Select filters for Question Search...',
            'data_sets': 'Select filters for Data Set Search...',
            'variables': 'Select filters for Variable Search...',
            'related_publications': 'Select filters for Publication Search...'
          },
          'clear-filters-tooltip': 'Click to unselect all filters',
          'uncollapse-filters-tooltip': {
            'true': 'Click to show selected filters',
            'false': 'Click to hide selected filters'
          },
          'filter-search-input-label': 'Filter by...',
          'study-filter': {
            'not-found': 'No study found!',
            'no-valid-selected': 'No valid study selected!'
          },
          'survey-filter': {
            'not-found': 'No survey found!',
            'no-valid-selected': 'No valid survey selected!'
          },
          'instrument-filter': {
            'not-found': 'No instrument found!',
            'no-valid-selected': 'No valid instrument selected!'
          },
          'question-filter': {
            'not-found': 'No question found!',
            'no-valid-selected': 'No valid question selected!'
          },
          'data-set-filter': {
            'not-found': 'No data set found!',
            'no-valid-selected': 'No valid data set selected!'
          },
          'variable-filter': {
            'not-found': 'No variable found!',
            'no-valid-selected': 'No valid variable selected!'
          },
          'related-publication-filter': {
            'not-found': 'No publication found!',
            'no-valid-selected': 'No valid publication selected!'
          },
          'panel-identifier-filter': {
            'not-found': 'No panel identifier found!',
            'no-valid-selected': 'No valid panel identifier selected!'
          },
          'derived-variables-identifier-filter': {
            'not-found': 'No derived variables identifier found!',
            'no-valid-selected': 'No valid derived variables identifier selected!'
          },
          'access-way-filter': {
            'not-found': 'No access way found!',
            'no-valid-selected': 'No valid access way selected!'
          },
          'study-series-filter': {
            'not-found': 'No existing study series found!',
            'no-valid-selected': 'No valid study series selected!'
          },
          'sponsor-filter': {
            'not-found': 'No existing sponsor found!',
            'no-valid-selected': 'No valid sponsor selected!'
          },
          'institution-filter': {
            'not-found': 'No existing institution found!',
            'no-valid-selected': 'No valid institution selected!'
          },
          'survey-method-filter': {
            'not-found': 'No existing survey method found!',
            'no-valid-selected': 'No valid survey method selected!'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', searchTranslations);
  });
