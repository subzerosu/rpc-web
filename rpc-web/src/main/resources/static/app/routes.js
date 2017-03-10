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
<<<<<<< HEAD
=======
        }, {
            name : 'app.tasks',
            url : '/tasks',
            component : 'task.list',
            resolve : {
                tasks : function(TaskService) {
                    return TaskService.getAllTasks();
                }
            },
            ncyBreadcrumb : {
                label : 'Задания',
            }
        }, {
            name : 'app.task',
            url : '/tasks/{taskId:[0-9]{1,4}}',
            component : 'task.detail',
            resolve : {
                // $transition$.params() may use instead of $stateParams
                task : function(TaskService, $stateParams) {
                    return TaskService.getTask($stateParams.taskId);
                }
            },
            ncyBreadcrumb : {
                parent : 'app.tasks',
                label : '{{$stateParams.taskId}}'
            }
>>>>>>> branch 'master' of https://github.com/subzerosu/rpc-web.git
        }];

        states.forEach(function(state) {
            $stateProvider.state(state);
        });
    };

})(angular);