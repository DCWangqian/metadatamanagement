'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController',
    function(entity) {
      var ctrl = this;
      ctrl.instrument = entity;
    });
