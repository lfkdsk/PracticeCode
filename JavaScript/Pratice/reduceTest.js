/**
 *
 * [1,2,3].reduce == func(func(1,2),3);
 * Created by liufengkai on 16/6/26.
 */


var arr = [12, 13, 14, 15];

console.log(arr.reduce(function (x, y) {
    return x * y;
}));

console.log(arr.reduce(function (x, y) {
    return x * 10 + y;
}));

var testString = ['1', '2', '3'];

console.log(testString.map(function (x) {
    return parseInt(x);
}));

