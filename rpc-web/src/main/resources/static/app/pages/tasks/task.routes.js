(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks', [ 'ui.router', 'ncy-angular-breadcrumb' ])

    // routing
    .config([ '$stateProvider', function($stateProvider) {

        var states = [ {
<<<<<<< HEAD
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
=======
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
>>>>>>> branch 'master' of https://github.com/subzerosu/rpc-web.git
                label : '{{$stateParams.taskId}}'
            }
        } ];

        states.forEach(function(state) {
            $stateProvider.state(state);
        });

    } ]);

})(angular);
