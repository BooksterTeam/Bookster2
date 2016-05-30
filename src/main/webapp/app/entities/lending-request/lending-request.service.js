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
                    data.created = DateUtils.convertLocalDateFromServer(data.created);
                    data.from = DateUtils.convertLocalDateFromServer(data.from);
                    data.due = DateUtils.convertLocalDateFromServer(data.due);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.created = DateUtils.convertLocalDateToServer(data.created);
                    data.from = DateUtils.convertLocalDateToServer(data.from);
                    data.due = DateUtils.convertLocalDateToServer(data.due);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.created = DateUtils.convertLocalDateToServer(data.created);
                    data.from = DateUtils.convertLocalDateToServer(data.from);
                    data.due = DateUtils.convertLocalDateToServer(data.due);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
