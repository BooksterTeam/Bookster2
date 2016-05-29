(function() {
    'use strict';
    angular
        .module('bookster2App')
        .factory('Lending', Lending);

    Lending.$inject = ['$resource', 'DateUtils'];

    function Lending ($resource, DateUtils) {
        var resourceUrl =  'api/lendings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.from = DateUtils.convertDateTimeFromServer(data.from);
                    data.due = DateUtils.convertDateTimeFromServer(data.due);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
