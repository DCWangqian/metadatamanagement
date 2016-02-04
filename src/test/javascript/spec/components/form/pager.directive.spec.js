describe('Unit testing metadatamanagementAppPage', function() {
  var $compile,
    $rootScope,
    $scope,
    $httpBackend,
    element;
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(inject(function(_$compile_, _$rootScope_, _$httpBackend_) {
    $compile = _$compile_;
    $scope = _$rootScope_.$new();
    $rootScope = _$rootScope_;
    $httpBackend = _$httpBackend_;
    var html =
      '<metadatamanagement-app-pager></metadatamanagement-app-pager>';
    $httpBackend.expectGET(
      'scripts/components/form/pager.html.tmpl').respond(200, {});
    element = $compile(html)($scope);
    $scope.$digest();
  }));

  describe('metadatamanagementAppPage', function() {
    it("Should display metadatamanagementAppPage", function() {
      expect(element).toBeDefined();
    });
  });
});
