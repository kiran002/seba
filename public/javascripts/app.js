$(function(){
  document.cookie.split('; ').forEach(function(cookieString) {
    var cookie;
    cookie = cookieString.split("=");
    if ((cookie.length === 2) && (cookie[0] === "stumarkAuthToken")) {
      return window.stumarkAuthToken = cookie[1];
    }
  });
});

var app = angular.module('stumark', [
  'ngRoute',
  'stumarkControllers',
  'stumarkServices'
]);

app.config(['$routeProvider', '$locationProvider',
  function($routeProvider, $locationProvider) {
    $routeProvider.
      when('/', {
        redirectTo: '/offers'
      }).
      when('/login', {
        templateUrl: '/assets/templates/login.html',
        controller: 'authController'
      }).
      when('/logout', {
        templateUrl: '/assets/templates/login.html',
        controller: 'authController'
      }).
      when('/register', {
        templateUrl: '/assets/templates/register.html',
        controller: 'registerController'
      }).
      when('/profile', {
        templateUrl: '/assets/templates/profile.html',
        controller: 'profileController'
      }).
      when('/offers', {
        templateUrl: '/assets/templates/offers.html',
        controller: 'listingController'
      }).
      when('/add_listing', {
        templateUrl: '/assets/templates/add_listing.html',
        controller: 'addListingController'
      }).
      when('/add_listing_img', {
        templateUrl: '/assets/templates/add_listing_img.html',
        controller: 'addListingImgController'
      }).
      when('/messages', {
          templateUrl: '/assets/templates/messages.html',
          controller: 'messagesController'
      }).
      otherwise({
        redirectTo: '/offers'
      });

      if(window.history && window.history.pushState){
        $locationProvider.html5Mode({
          enabled: true,
          requireBase: false
        });
      }
  }]);
