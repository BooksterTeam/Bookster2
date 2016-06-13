(function(){
    'use strict';
    angular
        .module('bookster2App')
        .factory('ReturnLending', ReturnLending);

    ReturnLending.$inject = ['$resource'];

    function ReturnLending($resource) {

        var resourceUrl = 'api/dashboard/return';

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
