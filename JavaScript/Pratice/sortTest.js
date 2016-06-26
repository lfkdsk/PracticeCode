/**
 * sort 方法会对原有的数据进行修改
 * -1 (x<y)
 * 0 (x=y)
 * 1 (x>y)
 * Created by liufengkai on 16/6/26.
 */

var arr = [10, 20, 1, 2];

var r = arr.sort(function (x, y) {
    if (x < y) {
        return -1;
    }

    if (x > y) {
        return 1;
    }
    return 0;
});

console.log(r);


arr = ['apple', 'Google', 'Microsoft'];

r = arr.sort(function (x, y) {
    x1 = x.toUpperCase();
    x2 = y.toUpperCase();

    if (x1 < x2) {
        return -1;
    }

    if (x1 > x2) {
        return 1;
    }
    return 0;
});

console.log(r);