(function(){
    'use strict';
    angular
        .module('bookster2App')
        .factory('DashLendingRequest', DashLendingRequest);

    DashLendingRequest.$inject = ['$resource'];

    function DashLendingRequest($resource) {

        var resourceUrl = 'api/dashboard/';

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
