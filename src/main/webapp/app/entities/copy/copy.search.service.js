(function() {
    'use strict';

    angular
        .module('bookster2App')
        .factory('CopySearch', CopySearch);

    CopySearch.$inject = ['$resource'];

    function CopySearch($resource) {
        var resourceUrl =  'api/_search/copies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
