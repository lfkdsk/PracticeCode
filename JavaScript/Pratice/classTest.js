/**
 * Created by liufengkai on 16/6/26.
 */

class Student {
    constructor(name) {
        this.name = name;
    }

    hello() {
        console.log('Hello ' + this.name + ' !');
    }

}

var ss = new Student('lfkdks');

ss.hello();


class PriStudent extends Student {
    constructor(name, grade) {
        super(name);
        this.grade = grade;
    }

    myGrade() {
        console.log('my grade is ' + this.grade);
    }
}

var pp = new PriStudent('sss',111);
pp.hello();
pp.myGrade();