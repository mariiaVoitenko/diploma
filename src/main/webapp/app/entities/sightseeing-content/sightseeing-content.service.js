(function() {
    'use strict';
    angular
        .module('diplomaApp')
        .factory('Sightseeing_content', Sightseeing_content);

    Sightseeing_content.$inject = ['$resource'];

    function Sightseeing_content ($resource) {
        var resourceUrl =  'api/sightseeing-contents/:id';

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
