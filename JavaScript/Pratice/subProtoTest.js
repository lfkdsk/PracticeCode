/**
 * Created by liufengkai on 16/6/26.
 */

function Student(props) {
    this.name = props.name;
}

Student.prototype.hello = function () {
    console.log(this.name + " is running!!!");
};

function PrimaryStudent(props) {
    Student.call(this, props);
    this.grade = props.grade || 1;
}

function F() {

}

F.prototype = Student.prototype;

PrimaryStudent.prototype = new F();

PrimaryStudent.prototype.constructor = PrimaryStudent;

PrimaryStudent.getGrade = function () {
    return this.grade;
};

function inherits(Child, Parent) {
    var F = function () {
    };
    F.prototype = Parent.prototype;
    Child.prototype = new F();
    Child.prototype.constructor = Child;
}

var smallMing = new PrimaryStudent({name: 'xiaoming', grade: 222});

smallMing.hello();

console.log(smallMing.name);