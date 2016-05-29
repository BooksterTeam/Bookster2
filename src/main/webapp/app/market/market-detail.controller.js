(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('MarketDetailController', MarketDetailController);

    MarketDetailController.$inject = ['$location','$log','$scope', '$rootScope', '$stateParams', 'entity', 'Book', 'Author', 'Copy', 'Tag'];

    function MarketDetailController($location, $log, $scope, $rootScope, $stateParams, entity, Book, Author, Copy, Tag) {
        var vm = this;
        vm.book = entity;
        vm.load = function (id) {
            Book.get({id: id}, function(result) {
                vm.book = result;
            });
        };
        //Following lines are needed if book is not found to display the typed id
        var path = $location.path();
        vm.fakeid = path.replace('/book/', '');
        var unsubscribe = $rootScope.$on('bookster2App:bookUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
