/**
 * Created by liufengkai on 16/6/26.
 */

function* nextCount(initial) {
    var x = initial || 0;
    for (let i = 0; i < 10; i++) {
        yield x;
        x += 20;
    }
    // x += 20;
    return x;
}

var fn = nextCount(10);

console.log(fn.next());
console.log(fn.next());
console.log(fn.next());
console.log(fn.next());