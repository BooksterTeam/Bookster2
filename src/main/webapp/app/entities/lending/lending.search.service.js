(function() {
    'use strict';

    angular
        .module('bookster2App')
        .factory('LendingSearch', LendingSearch);

    LendingSearch.$inject = ['$resource'];

    function LendingSearch($resource) {
        var resourceUrl =  'api/_search/lendings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
