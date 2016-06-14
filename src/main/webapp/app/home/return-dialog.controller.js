(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('ReturnController',ReturnController);

    ReturnController.$inject = ['$uibModalInstance', 'entity', 'ReturnLending', '$log'];

    function ReturnController($uibModalInstance, entity, ReturnLending, $log) {
        var vm = this;
        vm.lending = entity;
        $log.log(entity);
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmAccept = function (content) {
            $log.log(content)
            $log.log(vm.lending);
            ReturnLending.save(vm.lending,
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
