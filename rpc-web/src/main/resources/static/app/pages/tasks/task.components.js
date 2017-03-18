(function(angular) {
	'use strict';

	angular.module('rpcApp.tasks')
	
	// task list
	.component('task.list', {
        bindings: {
            tasks: '<'
        },
        templateUrl: 'app/pages/tasks/task.list.html',
        controller: TaskListController
    })
	
    // task detail
	.component('task.detail', {
        bindings: {
            task: '<'
        },
		templateUrl : 'app/pages/tasks/task.detail.html'
	});
	
	function TaskListController(TaskService, $state) {
	    var vm = this;
	    
	    vm.createTask = function() {
	        TaskService.createTask()
	            .then(function(resp) {
	                //TaskService.getAllTasks().push(resp.data);
	                $state.go("app.task", {taskId:resp.data.id});
                    console.log("task was submitted successfully!");
	            })
	            .catch(function(resp) {
                    console.log("problems during task submit!");
                })
	    };
	};

})(angular);
