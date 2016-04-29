'use strict';

describe('Controller Tests', function() {

    describe('Sightseeing_content Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSightseeing_content, MockContent, MockSightseeing;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSightseeing_content = jasmine.createSpy('MockSightseeing_content');
            MockContent = jasmine.createSpy('MockContent');
            MockSightseeing = jasmine.createSpy('MockSightseeing');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Sightseeing_content': MockSightseeing_content,
                'Content': MockContent,
                'Sightseeing': MockSightseeing
            };
            createController = function() {
                $injector.get('$controller')("Sightseeing_contentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'diplomaApp:sightseeing_contentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
