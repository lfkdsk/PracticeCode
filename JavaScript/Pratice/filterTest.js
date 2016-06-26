/**
 * filter 通过返回值确定一个值的去留
 * Created by liufengkai on 16/6/26.
 */

var arr = [1, 2, 4, 5, 6, 9, 10];

var r = arr.filter(function (x) {
    return x % 2 == 0;
});

console.log(r);