/**
 * Created by liufengkai on 16/6/25.
 */

function absTest(x) {

    if (typeof x !== 'number') {
        throw 'Not a number';
    }


    if (x >= 0) {
        return x;
    } else {
        return -x;
    }
}


var x = 10;

x = absTest(x);

console.log(x);

// absTest('flss');


function foo(x) {
    console.log('x -> ' + x);

    for (var i = 0; i < arguments.length; i++) {
        console.log(arguments[i]);
    }
}


foo(100, 'dddd', 'opopop', true);

// function lfkdskll(aa, bb, ...rest) {
//     console.log(rest);
// }

// lfkdskll(11, 22, 22222, 'fffff');


// function lfkdskll() {
//     for (let i = 0; i < 1000; i++) {
//         console.log(i);
//     }
// }

// lfkdskll();

var myself = {
    birth: 1996,
    age: function () {
        var y = new Date().getFullYear();
        return y - this.birth;
    }
};

console.log(myself.age());

