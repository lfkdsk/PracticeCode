/**
 * Created by liufengkai on 16/6/26.
 */

function Student(name) {
    this.name = name;
}

Student.prototype.hello = function () {
    console.log(this.name + " is running!!!");
};

var smallMing = new Student('SmallMing');

smallMing.hello();