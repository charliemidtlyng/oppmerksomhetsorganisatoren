'use strict';

angular.module('ooOppmerksomhetController', [])
    .factory('$oppmerksomhetsService', function() {
        return {
            hentInvolvertListe: function() {
                var personPromise = new $.Deferred();
                var adressePromise = new $.Deferred();
                $.get("/personer").success(function(result) {
                    var personer = _.map(result, function(person) {
                        return {
                            id: person.id,
                            navn: person.navn,
                            type: 'Person'
                        };
                    });
                    personPromise.resolve(personer);
                });
                $.get("/adresser").success(function(result) {
                    var adresser = _.map(result, function(adresse) {
                        return {
                            id: adresse.id,
                            navn: adresse.familienavn.concat(" - ").concat(adresse.adressenavn),
                            type: 'Familie'
                        };
                    });
                    adressePromise.resolve(adresser);
                });

                return $.when(personPromise, adressePromise);
            },
            slettOppmerksomhet: function(oppmerksomhet) {
                return $.post('/oppmerksomheter/' + oppmerksomhet.id + '/slett');
            },
            resolveInvolvert: function(involvert, involvertType, involvertListe) {
                return involvert && involvertType ? _.findWhere(involvertListe, {id: involvert.id}): {};
            },
            roller: function () {
                return ["Mottaker", "Giver"];
            },
            hendelser: function() {
                return ["Gave", "Gavekort", "Julekort", "Takkekort"];
            },
            lagreOppmerksomhet: function (oppmerksomhet) {
                return $.post('/oppmerksomheter', JSON.stringify(oppmerksomhet));
            }
        };
    })
    .controller('OppmerksomhetController', ['$scope', '$oppmerksomhetsService',
        function($scope, oppmerksomhetsService) {
            $scope.oppmerksomhet = {};
            $scope.hendelsestypeListe = oppmerksomhetsService.hendelser();
            $scope.rolleListe = oppmerksomhetsService.roller();
            $scope.oppmerksomheter = [];
            $scope.oppmerksomhetKalender = [];

            $scope.submitOppmerksomhet = function() {
                $scope.oppmerksomhet.fraType = $scope.oppmerksomhet.fra ? $scope.oppmerksomhet.fra.type : "";
                $scope.oppmerksomhet.tilType = $scope.oppmerksomhet.til ? $scope.oppmerksomhet.til.type : "";
                $scope.oppmerksomhet.verdi = $scope.oppmerksomhet.verdi ? $scope.oppmerksomhet.verdi.replace(/,/g, '.') : $scope.oppmerksomhet.verdi;
                $.post('/oppmerksomheter', JSON.stringify($scope.oppmerksomhet)).success(function() {
                    oppdaterOppmerksomheter();
                }).error(function(error) {
                    alert(error);
                });
            }

            function resolveName(item) {
                var erGiver = item.rolle === 'Giver';
                if (erGiver && item.til) {
                    return item.tilType === "Familie" ? item.til.adressenavn : item.til.navn;
                } else if (!erGiver && item.fra) {
                    return item.fraType === "Familie" ? item.fra.adressenavn : item.fra.navn;
                }
                return '';
            }

            var oppdaterOppmerksomheter = function() {
                return $.get("/oppmerksomheter").success(function(result) {
                    $scope.oppmerksomheter = result;
                    $scope.oppmerksomhetKalender = _.map(result, function(item) {
                        return {
                            id: item.id,
                            title: item.hendelsestype + ' ' + resolveName(item),
                            allDay: true,
                            start: new Date(item.tid),
                            url: '#/oppmerksomheter/' + item.id
                        }
                    });
                    $scope.eventSources[0] = $scope.oppmerksomhetKalender;
                    $scope.$apply();

                });
            }
            $scope.kalenderInnstillinger = {
                height: 450,
                editable: true,
                header: {
                    left: 'month basicWeek basicDay agendaWeek',
                    center: 'title',
                    right: 'today prev,next'
                },
                eventDrop: $scope.moveEvent
            };
            $scope.eventSources = [$scope.oppmerksomhetKalender];

            oppdaterOppmerksomheter();
            $.when(oppmerksomhetsService.hentInvolvertListe()).done(function(personer, adresser) {
                $scope.involvertListe = _.union(personer, adresser);
                $scope.$apply();
            });
        }
    ])
    .controller('EnkelOppmerksomhetController', ['$scope', '$routeParams', '$oppmerksomhetsService',
        function($scope, $routeParams, oppmerksomhetsService) {
            var id = $routeParams.id;
            $scope.hendelsestypeListe = oppmerksomhetsService.hendelser();
            $scope.rolleListe = oppmerksomhetsService.roller();
            
            $.when(oppmerksomhetsService.hentInvolvertListe()).done(function(personer, adresser) {
                $scope.involvertListe = _.union(personer, adresser);
                $.get("/oppmerksomheter/".concat(id)).success(function(result) {
                    console.log(result);
                    $scope.oppmerksomhet = result;
                    $scope.oppmerksomhet.til = oppmerksomhetsService.resolveInvolvert(result.til, result.tilType, $scope.involvertListe);
                    $scope.oppmerksomhet.fra = oppmerksomhetsService.resolveInvolvert(result.fra, result.fraType, $scope.involvertListe);
                    $scope.oppmerksomhet.tid = moment(result.tid).format('YYYY-MM-DD');
                    $scope.$apply();
                });
            });

            $scope.submitOppmerksomhet = function () {
                oppmerksomhetsService.lagreOppmerksomhet($scope.oppmerksomhet);
            }
        }
    ])

;