'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-set-management': {
        'log-messages': {
          'data-set': {
            'saved': 'Datensatz mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Datensatz mit FDZ-ID {{ id }} wurde nicht gespeichert!',
            'missing-id': 'Der {{ index }}. Datensatz enthält keine FDZ-ID und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ total }} Datensätzen mit {{ errors }} Fehlern beendet!',
            'cancelled': 'Upload von Datensätzen Abgebrochen',
            'unable-to-delete': 'Die Datensätze konnten nicht gelöscht werden!',
            'missing-sub-data-set-file': 'Keine Excel Datei mit dem Namen "{{ id }}.xlsx" gefunden!',
            'sub-data-set': {
              'number-of-observations-parse-error': 'Anzahl der Beobachtungen des Sub-Datensatzes {{name}} ist keine Zahl!',
              'number-of-analyzed-variables-parse-error': 'Anzahl der analysierten Variablen des Sub-Datensatzes {{name}} ist keine Zahl!'
            }
          },
          'tex': {
            'upload-terminated': 'Upload von Tex Template beendet!',
            'saved': 'Tex Dokument erfolgreich erzeugt!',
            'cancelled': 'Erzeugen von Tex Dokument Abgebrochen!'
          }
        },
        'home': {
          'title': 'Datensätze'
        },
        'detail': {
          'title': '{{ description }}',
          'data-set': 'Datensatz',
          'data-sets': 'Datensätze',
          'type': 'Typ',
          'data-set-informations': 'Datensatz Informationen',
          'related-information': 'Zugehörige Informationen',
          'data-set-same-study': 'Datensätze der gleichen Studie',
          'description': 'Beschreibung',
          'not-found': 'Die id {{id}} referenziert auf einen unbekannten Datensatz',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Datensätze.',
          'sub-data-sets': {
            'title': 'Datenprodukte',
            'name': 'Name',
            'accessWay': 'Datenprodukt',
            'description': 'Beschreibung',
            'numberOfAnalyzedVariables': ' Analysierbare Variablen',
            'numberOfObservations': 'Fälle'
          },
          'content': {
            'true': 'Klicken zum maximieren',
            'false': 'Klicken zum minimieren'
          },
          'generate-variable-report': 'Erzeuge Variablenreport',
          'no-related-data-sets': 'Keine zugehörige Datensätze.',
          'related-data-sets': 'Zugehörige Datensätze'
        },
        'error': {
          'files-in-template-zip-incomplete': 'Die bereitgestellten Dateien für die Erzeugung eines Datensatzreports sind unvollständig. Es fehlte die Datei: {{missingFile}}',
          'data-set': {
            'id': {
              'valid-data-set-id-name': 'Die FDZ-ID des Datensatzes entspricht nicht dem Muster: FDZID-ds{Number}.',
              'not-empty': 'Die FDZ-ID des Datensatzes darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 128 Zeichen.',
              'pattern': 'Die FDZ-ID darf nur alphanumerische Zeichen, deutsche Umlaute und ß beinhalten.'
            },
            'description': {
              'i18n-string-size': 'Die Maximallänge der Datensatzbeschreibung ist 2048 Zeichen.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'Die FDZ - ID des Projektes darf bei dem Datensatz nicht leer sein!'
              }
            },
            'variable': {
              'ids': {
                'not-empty': 'Der Datensatz muss mindestens eine Variable beinhalten!'
              }
            },
            'survey': {
              'ids': {
                'not-empty': 'Der Datensatz muss zu mindestens einer Erhebung gehören!'
              }
            },
            'sub-data-sets': {
              'not-empty': 'Der Datensatz musss mindestens einen Sub-Datensatz haben!'
            },
            'type': {
              'valid-type': 'Der Typ ist ungültig. Erlaubt sind nur: Personendatensatz oder Episodendatensatz.',
              'not-null': 'Der Typ des Datensatzes darf nicht leer sein!'
            }
          },
          'sub-data-set': {
            'name': {
              'not-empty': 'Der Name des {{index}}. Sub-Daten-Satzes darf bei dem Datensatz nicht leer sein!',
              'size': 'Die Maximallänge des Namens des {{index}}. Sub-Daten-Satzes ist 32 Zeichen.'
            },
            'description': {
              'i18n-string-not-empty': 'Die Beschreibung des {{index}}. Sub-Daten-Satzes darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung des {{index}}. Sub-Daten-Satzes ist 128 Zeichen.'
            },
            'access-way': {
              'not-null': 'Der Zugangsweg des {{index}}. Sub-Daten-Satzes darf nicht leer sein!',
              'valid-access-way': 'Der Zugangsweg des {{index}}. Sub-Daten-Satzes ist ungültig. Erlaubt sind nur: download-cuf, download-suf, remote-desktop-suf oder onsite-suf.'
            }
          },
          'post-validation': {
            'data-set-has-invalid-survey-id': 'Der Datensatz {{id}} referenziert eine unbekannte Erhebung({{toBereferenzedId}}).',
            'data-set-has-invalid-variable-id': 'Der Datensatz {{id}} referenziert eine unbekannte Variable({{toBereferenzedId}}).',
            'sub-data-set-has-an-accessway-which-was-not-found-in-study': 'Der Sub-Datensatz {{id}} hat einen Zugangsweg ({{toBereferenzedId}}) aufgelistet, der nicht den Zugangswegen der Studie gefunden wurde.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
