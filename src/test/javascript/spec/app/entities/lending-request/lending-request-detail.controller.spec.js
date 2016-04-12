'use strict';

describe('Controller Tests', function() {

    describe('LendingRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLendingRequest, MockLending, MockBooksterUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLendingRequest = jasmine.createSpy('MockLendingRequest');
            MockLending = jasmine.createSpy('MockLending');
            MockBooksterUser = jasmine.createSpy('MockBooksterUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LendingRequest': MockLendingRequest,
                'Lending': MockLending,
                'BooksterUser': MockBooksterUser
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
