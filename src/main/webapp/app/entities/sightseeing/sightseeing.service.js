(function() {
    'use strict';
    angular
        .module('diplomaApp')
        .factory('Sightseeing', Sightseeing);

    Sightseeing.$inject = ['$resource'];

    function Sightseeing ($resource) {
        var resourceUrl =  'api/sightseeings/:id';

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
