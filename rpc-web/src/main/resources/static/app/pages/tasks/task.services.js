(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks')
    
    // task service
    .service('TaskService', ['$http', function($http) {
        var service = {
            createTask: function(task) {
                    var data = {
                        task: task
                    };
                    return $http.post('http://localhost:5000/api/rpc/tasks/', 
                            JSON.stringify(data))
                    .then(function(resp) {
                        console.log("task was submitted successfully!");
                    }).catch(function(resp) {
                        console.log("problems during task submit!");
                    });
            },
                
                
            getAllTasks: function() {
                // return $http.get('app/data/tasks.json', {
                return $http.get('http://localhost:5000/api/rpc/tasks/', {
                    cache: true
                }).then(function(resp) {
                    return resp.data;
                }).catch(function(resp) {
                  return [];
                });
            },

            getTask: function(id) {
                function filterById(task) {
                    return task.id == id;
                }

                return service.getAllTasks().then(function(tasks) {
                    return tasks.find(filterById);
                });
            }
        };
        return service;
    }]);

})(angular);
