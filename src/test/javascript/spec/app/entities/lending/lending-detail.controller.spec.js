'use strict';

describe('Controller Tests', function() {

    describe('Lending Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLending, MockLendingRequest, MockBooksterUser, MockCopy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLending = jasmine.createSpy('MockLending');
            MockLendingRequest = jasmine.createSpy('MockLendingRequest');
            MockBooksterUser = jasmine.createSpy('MockBooksterUser');
            MockCopy = jasmine.createSpy('MockCopy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Lending': MockLending,
                'LendingRequest': MockLendingRequest,
                'BooksterUser': MockBooksterUser,
                'Copy': MockCopy
            };
            createController = function() {
                $injector.get('$controller')("LendingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bookster2App:lendingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
