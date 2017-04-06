'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'delete-messages': {
          'delete-variables-title': 'Alle Variablen ersetzen?',
          'delete-variables': 'Sind Sie sicher dass, Sie alle Variablen innerhalb des Datenaufbereitungsprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-surveys-title': 'Alle Erhebungen ersetzen?',
          'delete-surveys': 'Sind Sie sicher dass, Sie alle Erhebungen innerhalb des Datenaufbereitungsprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-questions-title': 'Alle Fragen löschen?',
          'delete-questions': 'Sind Sie sicher dass, Sie alle Fragen innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?',
          'delete-data-sets-title': 'Alle Datensätze löschen?',
          'delete-data-sets': 'Sind Sie sicher dass, Sie alle Datensätze innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?',
          'delete-studies-title': 'Studie löschen?',
          'delete-studies': 'Sind Sie sicher dass, Sie die Studie des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?',
          'delete-related-publications-title': 'Alle Publikationen löschen?',
          'delete-related-publications': 'Sind Sie sicher dass, Sie die Publikationen löschen möchten?',
          'delete-instruments-title': 'Alle Instrumente löschen?',
          'delete-instruments': 'Sind Sie sicher dass, Sie alle Instrumente innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?'
        },
        'detail': {
          'search': 'Suche'
        },
        'buttons': {
          'refresh-tooltip': 'Klicken, um die Suche zu aktualisieren',
          'upload-variables-tooltip': 'Kilcken, um Variablen für ausgewähltes Datenaufbereitungsprojekt hochzuladen',
          'upload-surveys-tooltip': 'Kilcken, um Erhebungen für ausgewähltes Datenaufbereitungsprojekt hochzuladen',
          'upload-data-sets-tooltip': 'Kilcken, um Datensätze für ausgewähltes Datenaufbereitungsprojekt hochzuladen',
          'upload-questions-tooltip': 'Kilcken, um Fragen für ausgewähltes Datenaufbereitungsprojekt hochzuladen',
          'upload-studies-tooltip': 'Kilcken, um Studie für ausgewähltes Datenaufbereitungsprojekt hochzuladen',
          'upload-related-publications-tooltip': 'Kilcken, um Publikationen hochzuladen',
          'post-validate-related-publications-tooltip': 'Kilcken, um Publikationen zu validieren',
          'upload-instruments-tooltip': 'Kilcken, um Instrumente für ausgewähltes Datenaufbereitungsprojekt hochzuladen'
        },
        'input-label': {
          'all': 'Suchen Sie Studien, Variablen, Fragen, Erhebungen, Datensätze, Instrumente oder Publikationen?',
          'variables': 'Suchen Sie Variablen?',
          'questions': 'Suchen Sie Fragen?',
          'surveys': 'Suchen Sie Erhebungen?',
          'data-sets': 'Suchen Sie Datensätze?',
          'studies': 'Suchen Sie Studien?',
          'related-publications': 'Suchen Sie Publikationen?',
          'instruments': 'Suchen Sie Instrumente?'
        },
        'tabs': {
          'variables': 'Variablen',
          'variables-tooltip': 'Klicken, um Tabreiter "Variablen" zu selektieren',
          'questions': 'Fragen',
          'questions-tooltip': 'Klicken, um Tabreiter "Fragen" zu selektieren',
          'surveys': 'Erhebungen',
          'surveys-tooltip': 'Klicken, um Tabreiter "Erhebungen" zu selektieren',
          'data_sets': 'Datensätze',
          'data_sets-tooltip': 'Klicken, um Tabreiter "Datensätze" zu selektieren',
          'studies': 'Studien',
          'studies-tooltip': 'Klicken, um Tabreiter "Studien" zu selektieren',
          'all': 'Alle',
          'all-tooltip': 'Klicken, um Tabreiter "Alle" zu selektieren',
          'related_publications': 'Publikationen',
          'related_publications-tooltip': 'Klicken, um Tabreiter "Publikationen" zu selektieren',
          'instruments': 'Instrumente',
          'instruments-tooltip': 'Klicken, um Tabreiter "Instrumente" zu selektieren'
        },
        'cards': {
          'question-tooltip': 'Klicken, um zur Frage zu navigieren',
          'variable-tooltip': 'Klicken, um zur Variable zu navigieren',
          'data-set-tooltip': 'Klicken, um zum Datensatz zu navigieren',
          'instrument-tooltip': 'Klicken, um zum Instrument zu navigieren',
          'survey-tooltip': 'Klicken, um zur Erhebung zu navigieren',
          'study-tooltip': 'Klicken, um zur Studie zu navigieren',
          'publication-tooltip': 'Klicken, um zur Publication zu navigieren'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', searchTranslations);
  });
