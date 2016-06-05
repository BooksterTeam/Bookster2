(function(){
    'use strict';
    angular
        .module('bookster2App')
        .factory('RejectLendingRequest', RejectLendingRequest);

    RejectLendingRequest.$inject = ['$resource'];

    function RejectLendingRequest($resource) {

        var resourceUrl = 'api/dashboard/reject';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

})();
