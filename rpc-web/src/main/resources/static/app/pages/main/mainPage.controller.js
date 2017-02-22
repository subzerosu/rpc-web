(function (angular) {
  'use strict';
  
  angular.module('rpcApp').controller('mainPageCtrl', ["$scope", "$timeout", "$mdSidenav", "$log", MainPageCtrl]);
  
  function MainPageCtrl($scope, $timeout, $mdSidenav, $log) {
	  $scope.toggleSidebar  = buildToggler('sidebar-left');
	  
	  function buildToggler(sidebarID) {
	      return function() {
	        $mdSidenav(sidebarID)
	          .toggle()
	          .then(function() {
	            $log.debug("toggle " + sidebarID + " is done");
	          });
	      };
	  }
  };
 
})(angular);
