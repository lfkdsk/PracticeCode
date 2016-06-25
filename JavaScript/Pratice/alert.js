/**
 * Created by liufengkai on 16/6/25.
 */

var fuckYou = "lfkdsk";

var lfk = `lfkdskdddd`;
console.log(lfk);

var arr = ['3.14', 3.14, fuckYou];

arr.forEach(function (element, value) {
    console.error(element + " num: " + value);
});

console.log(arr[5]);

module.exports = arr;

console.log(arr.indexOf(fuckYou));

arr[5] = 'lfkddd';

console.log(arr.shift());
console.log(arr.unshift("ha"));

arr.sort();
console.log(arr);

arr.reverse();
console.log(arr);

var list = ['f', 'u', 'c', 'k', 'y', 'o', 'u'];
// console.log(list.splice(2, 3, 'l', 'f', 'k'));
// console.log(list.splice(2, 2));
console.log(list.splice(2, 0, 'f', 'y'));
console.log(list);

var addList = list.concat(['lfkdsk', 'hahaha']);
console.log(addList);
console.log(addList.join('-'));

var newList = [[300, 3200], 'fffff', ['flkdks']];
console.log(newList[0][1]);