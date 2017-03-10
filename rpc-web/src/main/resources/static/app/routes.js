(function(angular) {
    'use strict';

    // routing
    angular.module('rpcApp').config(routerConfig);

    routerConfig.$inject = [ '$stateProvider', '$urlRouterProvider', '$breadcrumbProvider' ];
    function routerConfig($stateProvider, $urlRouterProvider, $breadcrumbProvider) {

        $breadcrumbProvider
                .setOptions({
                    prefixStateName : 'app',
                    includeAbstract : true,
                    template : '<li class="breadcrumb-item" ng-repeat="step in steps" ng-class="{active: $last}" ng-switch="$last || !!step.abstract"><a ng-switch-when="false" href="{{step.ncyBreadcrumbLink}}">{{step.ncyBreadcrumbLabel}}</a><span ng-switch-when="true">{{step.ncyBreadcrumbLabel}}</span></li>'
                });

        // default state
        $urlRouterProvider.otherwise('/dashboard');

        var states = [ {
            name : 'app',
            abstract : true,
            templateUrl : 'app/layout/layout.html',
            ncyBreadcrumb : {
                label : 'Root',
                skip : true
            }
        }, {
            name : 'app.dashboard',
            url : '/dashboard',
            component : 'dashboard',
            ncyBreadcrumb : {
                label : 'Главная',
            }
        }];

        states.forEach(function(state) {
            $stateProvider.state(state);
        });
    };

})(angular);