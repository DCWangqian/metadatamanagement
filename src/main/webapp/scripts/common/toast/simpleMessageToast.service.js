/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SimpleMessageToastService',
  function($mdToast) {
    var toastParent = angular.element('#toast-container');

    //The Toast for the upload complete
    function openSimpleMessageToast(messageId, messageParam) {
      $mdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
        hideDelay: 10000,
        position: 'top right',
        parent: toastParent,
        locals: {
          messageId: messageId,
          messageParam: messageParam
        }
      });
    }

    return {
      openSimpleMessageToast: openSimpleMessageToast
    };

  });
