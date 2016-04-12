(function() {
    'use strict';
    angular
        .module('bookster2App')
        .factory('BooksterUser', BooksterUser);

    BooksterUser.$inject = ['$resource'];

    function BooksterUser ($resource) {
        var resourceUrl =  'api/bookster-users/:id';

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
