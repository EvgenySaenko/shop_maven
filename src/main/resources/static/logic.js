var app = angular.module('app', ['ngRoute','ngStorage']);
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

app.controller('mainController', function ($scope, $http, $localStorage) {
    $scope.tryToAuth = function () {
        //когда нажмем на кнопку логин => пошлем запрос на /auth и подшиваем данные о пользователе $scope.user
        $http.post(contextPath + '/auth', $scope.user)//ng-model="user.username - вот с этих полей берем инфу(бэкенд это получит)
            .then(function (response) {//мы ожидаем респонс-(ожидаем Токен)
                if (response.data.token) {//если в этом json объекте есть поле токен
                    //к любым запросам отправленым через вот этот $http.defaults.headers(http блок) -  подшиваем стандартный
                    //подшиваем стандартный headers.common.Authorization, который бедет выглядеть - (Bearer и далее токен)
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    //закидываем в локальное хранилище инфу о юзере(нужно достать из токена) и в карент юзера зашили токен
                    $localStorage.currentUser = { username: $scope.user.username, token: response.data.token };
                }
            });
    };

    $scope.tryToLogout = function () {//если нажали кнопку выход
        delete $localStorage.currentUser;//удаляем запись о нашем юзере- фронтенд перестает помнить с кем он работает
        $http.defaults.headers.common.Authorization = '';//стираем инфу о токене- не подшиваем никакие хэдеры
    };
});

app.controller('shopController', function ($scope, $http, $localStorage) {
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    fillTable = function () {
        $http.get(contextPath + '/api/v1/products')// посылаем запрос на бэкенд
            .then(function (response) {//когда ответ прийдет мы сделаем то что тут
                $scope.ProductsList = response.data;
            });
    };

    fillTable();
});

app.controller('addOrEditProductController', function ($scope, $http, $routeParams, $localStorage) {
    const advertsPath = contextPath + '/api/v1/products';

    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    if ($routeParams.id != null) {
        $http.get(advertsPath + '/' + $routeParams.id).then(function (response) {
            $scope.productFromForm = response.data;
            console.log($scope.productFromForm);
        });
    }

    $scope.createOrUpdateProduct = function() {

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