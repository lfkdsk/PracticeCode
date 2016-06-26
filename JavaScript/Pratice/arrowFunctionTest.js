/**
 * ➡函数修复了词法作用域的this指针
 * Created by liufengkai on 16/6/26.
 */

var fun = x => {
    if (x < 0) {
        return x * x;
    } else {
        return -x * x;
    }
};

console.log(fun(111));

