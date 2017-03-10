(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks')
    
    // task service
    .service('TaskService', ['$http', function($http) {
        var service = {
            getAllTasks: function() {
                return $http.get('app/data/tasks.json', {
                    cache: true
                }).then(function(resp) {
                    return resp.data;
                }).catch(function(resp) {
                  return [];
                });
            },

            getTask: function(id) {
                function filterById(task) {
                    return task.id === id;
                }

                return service.getAllTasks().then(function(tasks) {
                    return tasks.find(filterById);
                });
            }
        };
        return service;
    }]);

})(angular);
