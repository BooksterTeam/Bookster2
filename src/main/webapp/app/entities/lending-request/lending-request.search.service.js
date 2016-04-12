(function() {
    'use strict';

    angular
        .module('bookster2App')
        .factory('LendingRequestSearch', LendingRequestSearch);

    LendingRequestSearch.$inject = ['$resource'];

    function LendingRequestSearch($resource) {
        var resourceUrl =  'api/_search/lending-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
