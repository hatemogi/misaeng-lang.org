(function 미생($) {
    var 코드미러_입히기 = function (el) {
        CodeMirror.fromTextArea(el, {
            lineNumbers: true,
            mode: "text/x-clojure"
        });
    };

    var 문서전체_코드미러_입히기 = function() {
        $("textarea").each(function(idx, el) {
            코드미러_입히기(el);
        });
    };

    $(문서전체_코드미러_입히기);
})(jQuery);
