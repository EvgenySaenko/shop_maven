angular.module('app').controller('cartController', function ($scope, $http, $location) {
    const contextPath = 'http://localhost:8189/market';

    fillTable = function () {
        $http.get(contextPath + '/api/v1/cart')
            .then(function (response) {
                $scope.CartList = response.data;
            });
    };

    $scope.createOrder = function () {
        $location.path('/create_order');
    }

    fillTable();
});