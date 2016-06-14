(function(){
    'use strict';
    angular
        .module('bookster2App')
        .factory('AcceptLendingRequest', AcceptLendingRequest);

    AcceptLendingRequest.$inject = ['$resource'];

    function AcceptLendingRequest($resource) {

        var resourceUrl = 'api/dashboard/accept';

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
