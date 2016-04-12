'use strict';

describe('Controller Tests', function() {

    describe('Copy Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCopy, MockBook, MockLending, MockBooksterUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCopy = jasmine.createSpy('MockCopy');
            MockBook = jasmine.createSpy('MockBook');
            MockLending = jasmine.createSpy('MockLending');
            MockBooksterUser = jasmine.createSpy('MockBooksterUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Copy': MockCopy,
                'Book': MockBook,
                'Lending': MockLending,
                'BooksterUser': MockBooksterUser
            };
            createController = function() {
                $injector.get('$controller')("CopyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bookster2App:copyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
