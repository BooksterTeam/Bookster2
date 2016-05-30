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
                    data.from = DateUtils.convertLocalDateFromServer(data.from);
                    data.due = DateUtils.convertLocalDateFromServer(data.due);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.from = DateUtils.convertLocalDateToServer(data.from);
                    data.due = DateUtils.convertLocalDateToServer(data.due);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.from = DateUtils.convertLocalDateToServer(data.from);
                    data.due = DateUtils.convertLocalDateToServer(data.due);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
