(function(angular) {
    'use strict';

    angular.module('rpcApp.tasks')
    
    // task service
    .service('TaskService', ['$http', function($http) {
        var taskUrl = 'http://localhost:5000/api/rpc/tasks';
        var cacheOpt = { cache: true };
        var service = {
            createTask: function() {
                    // TODO
                    var data = {
                        name: "task",
                        interval: 5
                    };
                    return $http.post(taskUrl, JSON.stringify(data));
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
//
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
