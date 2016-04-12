(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('TagDetailController', TagDetailController);

    TagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tag', 'Book'];

    function TagDetailController($scope, $rootScope, $stateParams, entity, Tag, Book) {
        var vm = this;
        vm.tag = entity;
        vm.load = function (id) {
            Tag.get({id: id}, function(result) {
                vm.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('bookster2App:tagUpdate', function(event, result) {
            vm.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
