var app = angular.module('app', ['ngRoute']);
var contextPath = 'http://localhost:8189/market'

app.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'homepage.html'
        })
        .when('/shop', {
            templateUrl: 'shop.html',
            controller: 'shopController'
        })
        .when('/add_or_edit_product', {
            templateUrl: 'add_or_edit_product.html',
            controller: 'addOrEditProductController'
        })
});

app.controller('shopController', function ($scope, $http) {
    window.btoa('11111111:100')

    fillTable = function () {
        $http.get(contextPath + '/api/v1/products')// посылаем запрос на бэкенд
            .then(function (response) {//когда ответ прийдет мы сделаем то что тут
                $scope.ProductsList = response.data;
            });
    };

    fillTable();
});

app.controller('addOrEditProductController', function ($scope, $http, $routeParams) {
    const advertsPath = contextPath + '/api/v1/products';

    if ($routeParams.id != null) {
        $http.get(advertsPath + '/' + $routeParams.id).then(function (response) {
            $scope.productFromForm = response.data;
            console.log($scope.productFromForm);
        });
    }

    $scope.createOrUpdateProduct = function() {
        window.btoa('11111111:100')

        if($scope.productFromForm.id == null) {
            $http.post(contextPath + '/api/v1/products', $scope.productFromForm).then(function (response) {
                console.log(response);
                window.location.href = contextPath + '/index.html#/shop';
                window.location.reload(true);
            });
        } else {
            $http.put(contextPath + '/api/v1/products', $scope.productFromForm).then(function (response) {
                console.log(response);
                window.location.href = contextPath + '/index.html#/shop';
                window.location.reload(true);
            });
        }
    };
});