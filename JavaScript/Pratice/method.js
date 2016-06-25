/**
 * Created by liufengkai on 16/6/25.
 */

var age = 20;

if (age >= 18) {
    console.log('adult');
} else {
    console.log('child');
}

var x = 0;

for (var i = 0; i < 10000; i++) {
    x += i;
}

console.log('x:' + x);


var lfk = {
    name: 'lfkdsk',
    index: 10
};

for (var key in lfk) {
    console.log(key);
}

x = 0;
var n = 99;
while (n > 0) {
    x += n;
    n -= 2;
}

console.log("x: " + x);

var m = new Map([['sssss', 'ddd']]);
m.set('lfkdsk', 1111);
m.set('lfkddd', 9090);

// [].forEach.call(m, function (element) {
//     console.log(element);
// });

m.forEach(function (value) {
    console.log("value: " + value);
});


/**
 * set 不支持重复
 * */
var SetExp = new Set([1, 1, 2, 3]);
console.log(SetExp);

var a = [1, 2, 3];

/**
 * 迭代器 for of
 * for each
 * for in(不建议会把另外的属性加入)
 */
 