(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks')
    
    // task service
    .service('TaskService', ['$http', '$state', function($http, $state) {
        var taskUrl = 'http://localhost:5000/api/rpc/tasks';
        var cacheOpt = { cache: true };
        
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
                    return resp.data;
                }).catch(function(resp) {
                  return undefined;
                });
                
//                function filterById(task) {
//                    return task.id == id;
//                }
//                return service.getAllTasks().then(function(tasks) {
//                    if(angular.isArray(tasks)) {
//                        return tasks.find(filterById);
//                    } else {
//                        return [];
//                    }
//                });
            }
        };
        return service;
    }]);

})(angular);
