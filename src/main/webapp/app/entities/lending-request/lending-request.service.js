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
                    data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
                    data.fromDate = DateUtils.convertLocalDateFromServer(data.fromDate);
                    data.dueDate = DateUtils.convertLocalDateFromServer(data.dueDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    data.fromDate = DateUtils.convertLocalDateToServer(data.fromDate);
                    data.dueDate = DateUtils.convertLocalDateToServer(data.dueDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    data.fromDate = DateUtils.convertLocalDateToServer(data.fromDate);
                    data.dueDate = DateUtils.convertLocalDateToServer(data.dueDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
