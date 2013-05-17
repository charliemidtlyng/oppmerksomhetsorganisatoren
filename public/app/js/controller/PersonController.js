'use strict';

angular.module('ooPersonController', [])

    .controller('PersonController', ['$scope', function ($scope) {

        $scope.adresseListe = [
            {id: 1, navn: "h0h0", postnummer: '0040', poststed: 'Oslo'},
            {id: 2, navn: "h0h02", postnummer: '0041', poststed: 'TRH'}
        ];

        $scope.personListe = [];
        $scope.person = {
            adresse: {}
        }

//        console.log(person.adresse)

        $scope.submitPerson = function() {
//            console.log('Submitted: ', $scope.person);
//            var sistePerson = _.last($scope.personListe);
//            $scope.person.id = sistePerson ? sistePerson.id + 1 : 1;
//            $scope.personListe.push(angular.copy($scope.person));
            $scope.person.adresse.adresseId = $scope.person.adresse.id;
            $.post('/personer', JSON.stringify($scope.person)).success(function(){
                fetchModel('personListe', '/personer');
            });
        }

        $scope.slettPerson = function(person) {
            $.post('/personer/' + person.id + '/slett').success(function(result){
                fetchModel('personListe', '/personer');
            });
        }

        function fetchModel(modelName, url) {
            $.get(url).success(function(result){
                $scope[modelName] = result;
                $scope.$apply()
            });
        }

        fetchModel('personListe', '/personer');
        fetchModel('adresseListe', '/adresser');

    }])
;