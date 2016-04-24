(function () {
    'use strict';
    angular
        .module('bookster2App')
        .controller('MarketController', MarketController)
        .filter;

    MarketController.$inject = ['$scope', '$state', 'Book', 'BookSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];
    
    function MarketController ($scope, $state, Book, BookSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
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

