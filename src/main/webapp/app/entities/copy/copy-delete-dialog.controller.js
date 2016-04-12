(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('CopyDeleteController',CopyDeleteController);

    CopyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Copy'];

    function CopyDeleteController($uibModalInstance, entity, Copy) {
        var vm = this;
        vm.copy = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Copy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
