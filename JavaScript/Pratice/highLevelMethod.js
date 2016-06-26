/**
 * Created by liufengkai on 16/6/26.
 */

function add(x, y, f) {
    return f(x) + f(y);
}

console.log(add(-5, 6, Math.abs));