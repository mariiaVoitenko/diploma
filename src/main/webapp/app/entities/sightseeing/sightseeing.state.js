(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sightseeing', {
            parent: 'entity',
            url: '/sightseeing?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Sightseeings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sightseeing/sightseeings.html',
                    controller: 'SightseeingController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('sightseeing-detail', {
            parent: 'entity',
            url: '/sightseeing/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Sightseeing'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sightseeing/sightseeing-detail.html',
                    controller: 'SightseeingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Sightseeing', function($stateParams, Sightseeing) {
                    return Sightseeing.get({id : $stateParams.id});
                }]
            }
        })
        .state('sightseeing.new', {
            parent: 'sightseeing',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sightseeing/sightseeing-dialog.html',
                    controller: 'SightseeingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                info: null,
                                latitude: null,
                                longitude: null,
                                photo: null,
                                rating: null,
                                votes_count: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sightseeing', null, { reload: true });
                }, function() {
                    $state.go('sightseeing');
                });
            }]
        })
        .state('sightseeing.edit', {
            parent: 'sightseeing',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sightseeing/sightseeing-dialog.html',
                    controller: 'SightseeingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sightseeing', function(Sightseeing) {
                            return Sightseeing.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sightseeing', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sightseeing.delete', {
            parent: 'sightseeing',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sightseeing/sightseeing-delete-dialog.html',
                    controller: 'SightseeingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sightseeing', function(Sightseeing) {
                            return Sightseeing.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sightseeing', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
