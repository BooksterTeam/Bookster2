'use strict';

describe('Controller Tests', function() {

    describe('BooksterUser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBooksterUser, MockCopy, MockLending, MockLendingRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBooksterUser = jasmine.createSpy('MockBooksterUser');
            MockCopy = jasmine.createSpy('MockCopy');
            MockLending = jasmine.createSpy('MockLending');
            MockLendingRequest = jasmine.createSpy('MockLendingRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BooksterUser': MockBooksterUser,
                'Copy': MockCopy,
                'Lending': MockLending,
                'LendingRequest': MockLendingRequest
            };
            createController = function() {
                $injector.get('$controller')("BooksterUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bookster2App:booksterUserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
