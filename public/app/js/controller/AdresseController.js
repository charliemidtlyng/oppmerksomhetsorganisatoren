'use strict';

angular.module('waAdresseController', [])

    .controller('AdresseController', ['$scope', function ($scope) {

        $scope.adresseListe = [
            {id: 1, navn: "h0h0", postnummer: '0040', poststed: 'Oslo'},
            {id: 2, navn: "h0h02", postnummer: '0041', poststed: 'TRH'}
            ];

        $scope.submitAdresse = function() {
            console.log('Submitted: ', $scope.adresse);
            var sisteAdresse = _.last($scope.adresseListe);
            $scope.adresse.id = sisteAdresse ? sisteAdresse.id + 1 : 1;
            $scope.adresseListe.push(angular.copy($scope.adresse))
        }

        $scope.slettAdresse = function(adresse) {
            $scope.adresseListe = _.reject($scope.adresseListe, function(element){
                return element.id === adresse.id;
            });
        }

    }])
;