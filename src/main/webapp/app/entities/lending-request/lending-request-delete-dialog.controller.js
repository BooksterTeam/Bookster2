(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingRequestDeleteController',LendingRequestDeleteController);

    LendingRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'LendingRequest'];

    function LendingRequestDeleteController($uibModalInstance, entity, LendingRequest) {
        var vm = this;
        vm.lendingRequest = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LendingRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
