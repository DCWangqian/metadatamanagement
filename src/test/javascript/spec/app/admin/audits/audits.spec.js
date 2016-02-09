'use strict';

describe('activate api', function() {

  describe('basic test', function() {
    it('should call $translate and $translatePartialLoader', inject(
      function($translate, $translatePartialLoader, $httpBackend,
        $state) {
        var globalJson = new RegExp('i18n\/.*\/global.json')
        var mainJson = new RegExp('i18n\/.*\/main.json');
        $httpBackend.whenGET(globalJson).respond({});
        $httpBackend.whenGET(mainJson).respond({});
        $httpBackend.expectGET(/api\/account\?cacheBuster=\d+/).respond(
          200, '');
        spyOn($translate, 'refresh').and.callThrough();
        spyOn($translatePartialLoader, 'addPart').and.callThrough();
        var config = $state.get('audits');
        expect(config.url).toEqual('/audits');
        expect(config.resolve.translatePartialLoader).toBeDefined;
        $state.go('audits');
        expect($translate.refresh).toHaveBeenCalled();
        expect($translatePartialLoader.addPart).toHaveBeenCalled();
      }));
  });
});
