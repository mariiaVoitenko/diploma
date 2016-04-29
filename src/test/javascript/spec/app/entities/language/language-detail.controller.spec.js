'use strict';

describe('Controller Tests', function() {

    describe('Language Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLanguage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLanguage = jasmine.createSpy('MockLanguage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Language': MockLanguage
            };
            createController = function() {
                $injector.get('$controller')("LanguageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'diplomaApp:languageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
