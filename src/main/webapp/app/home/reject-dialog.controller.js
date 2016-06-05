(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('RejectController',RejectController);

    RejectController.$inject = ['$uibModalInstance', 'entity', 'RejectLendingRequest', '$log'];

    function RejectController($uibModalInstance, entity, RejectLendingRequest, $log) {
        var vm = this;
        vm.lendingRequest = entity;
        $log.log(entity)
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmReject = function (content) {
            $log.log(content)
            $log.log(vm.lendingRequest);
            RejectLendingRequest.save(vm.lendingRequest,
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
