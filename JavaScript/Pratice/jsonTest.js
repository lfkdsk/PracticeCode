/**
 * Created by liufengkai on 16/6/26.
 */

var smallMing = {
    name: "lfkdks",
    height: 1.83
};

console.log(JSON.stringify(smallMing, null, ' '));


console.log(JSON.stringify(smallMing, (function (key, value) {
    if (typeof value === 'string') {
        return value.toUpperCase();
    }
    return value;
}), ' '));

var jsonSmallMing = '{"name":"fuck","height":1222}';

console.log(JSON.parse(jsonSmallMing));