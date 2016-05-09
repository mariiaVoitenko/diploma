'use strict';

describe('Controller Tests', function() {

    describe('Code Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCode, MockSightseeing;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCode = jasmine.createSpy('MockCode');
            MockSightseeing = jasmine.createSpy('MockSightseeing');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Code': MockCode,
                'Sightseeing': MockSightseeing
            };
            createController = function() {
                $injector.get('$controller')("CodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'diplomaApp:codeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
