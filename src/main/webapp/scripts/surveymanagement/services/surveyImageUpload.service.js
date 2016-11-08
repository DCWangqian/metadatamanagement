'use strict';

angular.module('metadatamanagementApp').service('SurveyImageUploadService',
  function(Upload, $q) {
    var uploadImage = function(image, imageId, fileName) {
      var deferred = $q.defer();
      Upload.upload({
        url: '/api/surveys/images',
        fields: {
          'id': imageId,
          'filename': fileName,
          'image': image
        },
      }).success(function() {
        deferred.resolve();
      }).error(function(error) {
        deferred.reject(error);
      });
      return deferred.promise;
    };
    return {
      uploadImage: uploadImage
    };
  });
