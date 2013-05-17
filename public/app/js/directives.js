angular.module('ooDirectives', [])

    .directive('datepicker', function($parse){
        var directiveDefinitionObject = {
            restrict: 'A',
            link: function postLink(scope, iElement, iAttrs) {
                iElement.datepicker({
                    format: 'yyyy-mm-dd'
                }).on('changeDate', function(){
                    $(this).datepicker('hide');
                    var value = $(this).val();
                    scope.$apply(function(scope){
                        $parse(iAttrs.ngModel).assign(scope, value);
                    });


                    });
            }
        };
        return directiveDefinitionObject;

    })
    .directive('activeTab', ['$location', function(location) {
        return {
            restrict: 'AC',
            link: function(scope, ulElement) {
                var linkElements = $(ulElement).children();
                var links = {};

                linkElements.each(function() {
                    var $li = $(this);
                    var href = $li.find('a').attr('href');

                    if(href && href.length > 0) {
                        var hrefWithoutHashBang = href.substring(1);
                        links[hrefWithoutHashBang] = $li;
                    }
                });

                scope.$watch(function() { return location.path() }, function(newPath) {

                    linkElements.removeClass("active");
                    var link = links[newPath];
                    if(link){
                        link.addClass("active");
                    }
                });
            }

        }
    }]);

    ;