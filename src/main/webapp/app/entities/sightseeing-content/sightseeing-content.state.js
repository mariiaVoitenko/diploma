(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sightseeing-content', {
            parent: 'entity',
            url: '/sightseeing-content?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Sightseeing_contents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sightseeing-content/sightseeing-contents.html',
                    controller: 'Sightseeing_contentController',
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
        .state('sightseeing-content-detail', {
            parent: 'entity',
            url: '/sightseeing-content/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Sightseeing_content'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sightseeing-content/sightseeing-content-detail.html',
                    controller: 'Sightseeing_contentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Sightseeing_content', function($stateParams, Sightseeing_content) {
                    return Sightseeing_content.get({id : $stateParams.id});
                }]
            }
        })
        .state('sightseeing-content.new', {
            parent: 'sightseeing-content',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sightseeing-content/sightseeing-content-dialog.html',
                    controller: 'Sightseeing_contentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sightseeing-content', null, { reload: true });
                }, function() {
                    $state.go('sightseeing-content');
                });
            }]
        })
        .state('sightseeing-content.edit', {
            parent: 'sightseeing-content',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sightseeing-content/sightseeing-content-dialog.html',
                    controller: 'Sightseeing_contentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sightseeing_content', function(Sightseeing_content) {
                            return Sightseeing_content.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sightseeing-content', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sightseeing-content.delete', {
            parent: 'sightseeing-content',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sightseeing-content/sightseeing-content-delete-dialog.html',
                    controller: 'Sightseeing_contentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sightseeing_content', function(Sightseeing_content) {
                            return Sightseeing_content.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sightseeing-content', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
