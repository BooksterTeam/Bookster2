'use strict';

describe('Controller Tests', function() {

    describe('LendingRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLendingRequest, MockBooksterUser, MockCopy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLendingRequest = jasmine.createSpy('MockLendingRequest');
            MockBooksterUser = jasmine.createSpy('MockBooksterUser');
            MockCopy = jasmine.createSpy('MockCopy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LendingRequest': MockLendingRequest,
                'BooksterUser': MockBooksterUser,
                'Copy': MockCopy
            };
            createController = function() {
                $injector.get('$controller')("LendingRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bookster2App:lendingRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
