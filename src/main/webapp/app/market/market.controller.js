(function () {
    'use strict';
    angular
        .module('bookster2App')
        .controller('MarketController', MarketController)
        .filter;

    MarketController.$inject = ['$scope', '$state', 'Book', 'Market', 'BookSearch', 'ParseLinks', 'AlertService'];
    
    function MarketController ($scope, $state, Market, Book, BookSearch, ParseLinks, AlertService) {
        var vm = this;
        $scope.books = [];
        $scope.query = '';
        $scope.loadAll = function () {
            Market.query({page: $scope.page, size: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.books = result;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();
    }
})();

