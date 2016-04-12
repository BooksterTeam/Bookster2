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
                    data.fromDate = DateUtils.convertLocalDateFromServer(data.fromDate);
                    data.dueDate = DateUtils.convertLocalDateFromServer(data.dueDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fromDate = DateUtils.convertLocalDateToServer(data.fromDate);
                    data.dueDate = DateUtils.convertLocalDateToServer(data.dueDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fromDate = DateUtils.convertLocalDateToServer(data.fromDate);
                    data.dueDate = DateUtils.convertLocalDateToServer(data.dueDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
