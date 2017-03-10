(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks', [ 'ui.router', 'ncy-angular-breadcrumb' ])

    // routing
    .config([ '$stateProvider', function($stateProvider) {

        var states = [ {
            name : 'app.task.list',
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
            name : 'app.task.detail',
            url : '/tasks/{taskId:[0-9]{1,4}}',
            component : 'task.detail',
            resolve : {
                // $transition$.params() may use instead of $stateParams
                task : function(TaskService, $stateParams) {
                    return TaskService.getTask($stateParams.taskId);
                }
            },
            ncyBreadcrumb : {
                parent : 'app.task.list',
                label : '{{$stateParams.taskId}}'
            }
        } ];

        states.forEach(function(state) {
            $stateProvider.state(state);
        });

    } ]);

})(angular);
