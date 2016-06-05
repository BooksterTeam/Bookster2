(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$log', 'DashLendingRequest'];

    function HomeController ($scope, Principal, LoginService, $log, DashLendingRequest) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.lendingRequests = [];
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        $scope.loadLendingRequests = function(){
            DashLendingRequest.get(function (data) {
                vm.lendingRequests = data.lendingRequests;
                vm.lendings = data.lendings;
                vm.copies = data.copies;
            });
        }
        $scope.loadLendingRequests();
    }
})();
