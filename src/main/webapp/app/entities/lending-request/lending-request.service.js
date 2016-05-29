(function() {
    'use strict';
    angular
        .module('bookster2App')
        .factory('LendingRequest', LendingRequest);

    LendingRequest.$inject = ['$resource', 'DateUtils'];

    function LendingRequest ($resource, DateUtils) {
        var resourceUrl =  'api/lending-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
