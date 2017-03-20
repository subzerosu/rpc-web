(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks', [ 'ui.router', 'ncy-angular-breadcrumb' ])

    // routing
    .config([ '$stateProvider', function($stateProvider) {

        var states = [ {
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
        }, {
            name : 'app.newtask',
            url : '/tasks/new',
            component : 'task.new',
            resolve : {
                newTask : function() {
                    return {
                        name: "Проверка таблицы баркодов"
                    }
                }
            },
            ncyBreadcrumb : {
                parent : 'app.tasks',
                label : 'Новое'
            }
        }, {
            name : 'app.edittask',
            url : '/tasks/{taskId:[0-9]{1,4}}/edit',
            component : 'task.edit',
            resolve : {
                task : function(TaskService, $stateParams) {
                    return TaskService.getTask($stateParams.taskId);
                }
            },
            ncyBreadcrumb : {
                parent : 'app.tasks',
                label : '{{$stateParams.taskId}}'
            }
        } ];

        states.forEach(function(state) {
            $stateProvider.state(state);
        });

    } ]);

})(angular);
