(function(angular) {
	'use strict';
	var ctrl_name = 'mainCtrl';
	var url_pattern = '/';

	angular.module('rpcApp').controller(
			ctrl_name,
			[ "$scope", "$location", "$http", "$mdSidenav", "$log",
					MainController ]).config([ '$routeProvider', MainConfig ]);

	function MainController($scope, $location, $http, $mdSidenav, $log) {
		var self = this;
		$scope.name = ctrl_name;

		self.navItems = [{
			label : 'Главная',
			name : 'mainPage'
		}, {
			label : 'Задания',
			name : 'taskPage'
		}, {
			label : 'Источник',
			name : 'sourcePage'
		}, {
			label : 'Настройки',
			name : 'configPage'
		}];

		$scope.currentNavItem = 'taskPage';
		$scope.currentNavLabel = 'Задания';
		
		$scope.$watch('currentNavItem', function(it) {
			$scope.currentNavLabel = self.getByName(it);
	    });
		
		self.getByName = function(itemname) {
			var itemName = itemname;
			var positiveArr = self.navItems.filter(function(item) {
				  return itemName == item.name;
			});
			if(Array.isArray(positiveArr) && positiveArr.length) {
				return positiveArr[0].label;
			}
		};
	};

	function MainConfig($routeProvider) {
		$routeProvider.when(url_pattern, {
			templateUrl : './app/pages/main.html',
			controller : ctrl_name
		}).otherwise({
			templateUrl : './app/pages/main.html'
		});
	};

})(angular);
