/**
 * Created by liufengkai on 16/6/26.
 */

var count = 0;
var oldParseInt = parseInt;

global.parseInt = function () {
    count += 1;
    return oldParseInt.apply(null, arguments);
};

parseInt('10');
parseInt('20');

console.log(count);