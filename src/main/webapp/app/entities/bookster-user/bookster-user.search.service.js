(function() {
    'use strict';

    angular
        .module('bookster2App')
        .factory('BooksterUserSearch', BooksterUserSearch);

    BooksterUserSearch.$inject = ['$resource'];

    function BooksterUserSearch($resource) {
        var resourceUrl =  'api/_search/bookster-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
