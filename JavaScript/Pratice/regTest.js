/**
 * Created by liufengkai on 16/6/26.
 */

var reg = /^\d{3}\-\d{3,8}$/;

console.log(reg.test('010-12345'));

console.log('a b    c   d'.split(/\s+/));

var re = /^(\d{3})-(\d{3,8})/;

console.log(re.exec('010-12345'));

