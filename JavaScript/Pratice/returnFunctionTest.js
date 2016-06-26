/**
 * 返回闭包不要引用任何循环计数
 *
 * 闭包是携带状态的函数
 * Created by liufengkai on 16/6/26.
 */

function sum(arr) {
    return arr.reduce(function (x, y) {
        return x + y;
    });
}

var r = sum([1, 2, 3, 4]);

console.log(r);

function count() {
    var arr = [];
    for (var i = 1; i <= 3; i++) {
        arr.push((function (n) {
            // return function () {
            //     return n * n;
            // }
            return n * n;
        })(i));
    }
    return arr;
}

var results = count();

var f1 = results[0];
var f2 = results[1];
var f3 = results[2];

console.log(f1);
console.log(f2);
console.log(f3);

var lfkdsk = {
    name: "lfk",
    lll: function () {
        return this.name;
    }
};

console.log(lfkdsk.lll());


function create_counter(initial) {
    var x = initial || 0;
    return {
        inc: function () {
            x += 200;
            return x;
        }
    }
}

var fun = create_counter;
console.log(create_counter().inc());
console.log(create_counter(100).inc());

console.log(fun(111).inc());
console.log(fun(1).inc());