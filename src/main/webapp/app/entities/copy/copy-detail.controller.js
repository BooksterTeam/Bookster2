(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('CopyDetailController', CopyDetailController);

    CopyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Copy', 'Book', 'BooksterUser'];

    function CopyDetailController($scope, $rootScope, $stateParams, entity, Copy, Book, BooksterUser) {
        var vm = this;
        vm.copy = entity;
        vm.load = function (id) {
            Copy.get({id: id}, function(result) {
                vm.copy = result;
            });
        };
        var unsubscribe = $rootScope.$on('bookster2App:copyUpdate', function(event, result) {
            vm.copy = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
