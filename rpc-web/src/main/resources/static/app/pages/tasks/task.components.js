(function(angular) {
	'use strict';

	angular.module('rpcApp.tasks')
	
	// task list
	.component('task.list', {
        bindings: {
            tasks: '<'
        },
        templateUrl: 'app/pages/tasks/task.list.html'
    })
	
    // task detail
	.component('task.detail', {
        bindings: {
            task: '<'
        },
		templateUrl : 'app/pages/tasks/task.detail.html'
	})
	
	// task new
    .component('task.new', {
        bindings: {
            newTask: '<'
        },
        templateUrl: 'app/pages/tasks/task.new.html',
        controller: TaskNewController
    })
    
    // task edit
    .component('task.edit', {
        bindings: {
            task: '<'
        },
        templateUrl: 'app/pages/tasks/task.edit.html',
        controller: TaskEditController
    });

	function TaskNewController(TaskService, $state) {
	        var vm = this;
	        vm.addTask = function(newTask) {
	            TaskService.createTask(newTask);
	        };
	};
	
	function TaskEditController(TaskService, $state) {
           var vm = this;
           vm.saveTask = function(task) {
               TaskService.updateTask(task);
           };
    };

})(angular);
