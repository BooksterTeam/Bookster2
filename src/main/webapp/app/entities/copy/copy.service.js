(function() {
    'use strict';
    angular
        .module('bookster2App')
        .factory('Copy', Copy);

    Copy.$inject = ['$resource'];

    function Copy ($resource) {
        var resourceUrl =  'api/copies/:id';

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
