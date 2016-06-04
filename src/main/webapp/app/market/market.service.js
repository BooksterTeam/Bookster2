(function(){
    'use strict';
    angular
        .module('bookster2App')
        .factory('Market', Market);

    Market.$inject = ['$resource'];

    function Market($resource) {

        var resourceUrl = 'api/market/borrow';

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
