(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks')
    
    // task service
    .service('TaskService', ['$http', '$state', 'Notification', function($http, $state, Notification) {
        var taskUrl = 'http://localhost:5000/api/rpc/tasks';
        var cacheOpt = { cache: false };
        
        var service = {
            // new task
            createTask: function(newTask) {
                return $http.post(taskUrl, JSON.stringify(newTask))
                    .then(function(resp) {
                        // TaskService.getAllTasks().push(resp.data);
                        $state.go("app.task", {taskId:resp.data.id});
                        console.log("task was submitted successfully!");
                    })
                    .catch(function(resp) {
                        console.log("problems during task submit!");
                    })
            },
            
            // save task
            updateTask: function(task) {
                return $http.put(taskUrl + '/' + task.id, JSON.stringify(task))
                    .then(function(resp) {
                        $state.go("app.task", {taskId:resp.data.id});
                        console.log("task was updated successfully!");
                    })
                    .catch(function(resp) {
                        console.log("problems during task submit!");
                    })
            },
                
            getAllTasks: function() {
                // return $http.get('app/data/tasks.json', {
                return $http.get(taskUrl).then(function(resp) {
                    return resp.data;
                }).catch(function(resp) {
                  return [];
                });
            },

            getTask: function(id) {
                return $http.get(taskUrl + '/' + id, cacheOpt)
                .then(function(resp) {
                    if(resp.data) {
                        return resp.data;
                    } else {
                        console.log("задание не найдено.")
                        $state.go("app.undeftask");
                    }
                }).catch(function(resp) {
                    console.log("задание не найдено.")
                    $state.go("app.undeftask");
                  // return undefined;
                });
                
// function filterById(task) {
// return task.id == id;
// }
// return service.getAllTasks().then(function(tasks) {
// if(angular.isArray(tasks)) {
// return tasks.find(filterById);
// } else {
// return [];
// }
// });
            },
            
            startTask: function(task) {
                var oper = { 
                        operation: 'start' 
                };
                return $http.put(taskUrl + '/' + task.id + '/operation', JSON.stringify(oper))
                .then(function(resp) {
                    Notification.primary('Задача запущена!');
                    console.log("task was started successfully!");
                })
                .catch(function(resp) {
                    Notification.primary('Задачу запустить не удалось!');
                    console.log("problems during task starting!");
                })
            },
        
            stopTask: function(task) {
                var oper = { 
                        operation: 'stop' 
                };
                return $http.put(taskUrl + '/' + task.id + '/operation', JSON.stringify(oper))
                .then(function(resp) {
                    Notification.primary('Задача остановлена!');
                    console.log("task was stopped successfully!");
                })
                .catch(function(resp) {
                    Notification.primary('Задачу остановить не удалось!');
                    console.log("problems during task stoping!");
                })
            }
        };
        return service;
    }]);

})(angular);
