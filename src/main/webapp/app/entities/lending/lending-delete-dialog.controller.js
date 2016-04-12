(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingDeleteController',LendingDeleteController);

    LendingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lending'];

    function LendingDeleteController($uibModalInstance, entity, Lending) {
        var vm = this;
        vm.lending = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Lending.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
