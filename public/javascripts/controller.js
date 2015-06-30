var stumarkControllers = angular.module('stumarkControllers', []);

stumarkControllers.controller('listingController', ['$scope', '$http', 'Util', 'Listing',
  function($scope, $http, Util, Listing) {
    $scope.util = Util;
    $scope.listing = Listing;
    $scope.offers = $scope.listing.offers();
  }]);

stumarkControllers.controller('registerController', ['$scope', '$http', 'Util', 'User',
  function($scope, $http, Util, User) {
    $scope.util = Util;
    $scope.alert = Util.alert;
    $scope.disabled = false;
    $scope.alert.msg = '';
    var showAlert = function(msg, hasError){
      $scope.alert.hasError = hasError;
      $scope.alert.msg = msg;
      scrollTo(0, 0);
    };

    $scope.user = {
      firstname: '',
      lastname: '',
      email: '',
      password: '',
      password2: ''
    };
    var default_user = angular.copy($scope.user);

    $scope.submit = function(){
      if(!$scope.register.$valid){
        showAlert('Required field(s) are either missing or invalid', true);
        return;
      }

      if($scope.user.password != $scope.user.password2){
        showAlert('Passwords don\'t match', true);
        return;
      }

      var data = {
        fname: $scope.user.firstname,
        lname: $scope.user.lastname,
        email: $scope.user.email,
        password: $scope.user.password
      };

      $scope.disabled = true;
      $http.post('/api/register', data).
        success(function(data, status) {
          $scope.disabled = false;
          $scope.user = angular.copy(default_user);
          showAlert('Your account has been created. Please login and activate your account to be able to add listings.', false);
          console.log(data);
          console.log(status);
        }).
        error(function(data, status) {
          $scope.disabled = false;
          showAlert('ERROR', true);
          console.log(data);
          console.log(status);
        });
    }
  }]);

stumarkControllers.controller('authController', ['$scope', '$http', 'Util', 'User',
  function($scope, $http, Util, User) {
    $scope.util = Util;
    $scope.alert = Util.alert;
    $scope.disabled = false;
    $scope.alert.msg = '';
    var showAlert = function(msg, hasError){
      $scope.alert.hasError = hasError;
      $scope.alert.msg = msg;
      scrollTo(0, 0);
    };

    $scope.user = {
      email: '',
      password: ''
    };

    $scope.submit = function(){
      if(!$scope.login.$valid){
        showAlert('Required field(s) are either missing or invalid', true);
        return;
      }

      var data = {
        email: $scope.user.email,
        password: $scope.user.password
      };

      $scope.disabled = true;
      $http.post('/api/login', data).
        success(function(data, status) {
          $scope.disabled = false;
          console.log(data);
          console.log(status);
          // save user data in Util.currentUser
          // change header to logged in version
          // redirect to home page
          // setup session
          var d = new Date();
          var expire_days = 30;
          d.setTime(d.getTime() + (expire_days*24*60*60*1000));
          document.cookie="stumarkAuthToken=yu34ojh3ii4kh432; expires=" + d.toGMTString();
          document.location = '/';
        }).
        error(function(data, status) {
          $scope.disabled = false;
          showAlert('ERROR', true);
          console.log(data);
          console.log(status);
        });
    }
  }]);

stumarkControllers.controller('profileController', ['$scope', '$http', 'Util', 'User',
  function($scope, $http, Util, User) {
    $scope.util = Util;
  }]);

stumarkControllers.controller('addListingController', ['$scope', '$http', 'Util', 'Listing',
  function($scope, $http, Util, Listing) {
    $scope.util = Util;
    $scope.alert = Util.alert;
    $scope.disabled = (Util.isAuth()) ? false : true;
    $scope.alert.msg = (Util.isAuth()) ? '' : 'You need to be logged in to add a listing';
    var showAlert = function(msg, hasError){
      $scope.alert.hasError = hasError;
      $scope.alert.msg = msg;
      scrollTo(0, 0);
    };

    $scope.data = {
      listingType: Listing.getListingType(),
      transactionType: Listing.getTransactionType(),
      pricePeriod: Listing.getPricePeriod(),
      category: Listing.getCategory()
    };

    $scope.listing = {
      id: 0,
      userId: Util.currentUser.id,
      listingType: angular.copy($scope.data.listingType[0].id),
      name: '',
      category: angular.copy($scope.data.category[0].id),
      description: '',
      transactionType: angular.copy($scope.data.transactionType[0].id),
      price: '',
      pricePeriod: angular.copy($scope.data.pricePeriod[0].id),
      availFrom: '',
      availUntil: '',
      expire: ''
    };

    $scope.submit = function(){
      if(!$scope.addListing.$valid){
        showAlert('Required field(s) are either missing or invalid', true);
        return;
      }

      var data = {
        name: $scope.listing.name,
        userId: Util.currentUser.id,
        category: $scope.listing.category,
        description: $scope.listing.description,
        listingtype: $scope.listing.listingType,
        price: $scope.listing.price,
        period: $scope.listing.pricePeriod,
        transactiontype: $scope.listing.transactionType,
        transactionStart: $scope.listing.availFrom,
        transactionEnd: $scope.listing.availUntil,
        transactionExpire: $scope.listing.expire
      };

      $scope.disabled = true;
      $http.post('/api/listing', data).
        success(function(data, status) {
          $scope.disabled = false;
          console.log(data);
          console.log(status);
          showAlert('Lisitng added', false);
        }).
        error(function(data, status) {
          $scope.disabled = false;
          showAlert('ERROR', true);
          console.log(data);
          console.log(status);
        });
    }
  }]);

stumarkControllers.controller('addListingImgController', ['$scope', '$http', 'Util', 'Listing',
  function($scope, $http, Util, Listing) {
    $scope.util = Util;
    $scope.alert = Util.alert;
    $scope.disabled = (Util.isAuth()) ? false : true;
    $scope.alert.msg = (Util.isAuth()) ? '' : 'You need to be logged in to add a listing';
    var showAlert = function(msg, hasError){
      $scope.alert.hasError = hasError;
      $scope.alert.msg = msg;
      scrollTo(0, 0);
    };

    $scope.loadData = function () {
       $scope.images = Listing.getImageList(1);
    };

    //initial load
    $scope.loadData();

  }]);

stumarkControllers.controller('messagesController', ['$scope', '$http', 'Util', 'Listing',
    function($scope, $http, Util, Listing) {
      $scope.util = Util;
      $scope.listing = Listing;
    }
]);
