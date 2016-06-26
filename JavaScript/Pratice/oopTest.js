/**
 * Created by liufengkai on 16/6/26.
 */

var Student = {
    name: "lfkdsk",
    height: 1.2,
    run: function() {
        return this.name +" is running";
        // console.log(this instanceof Student);
        // console.log(this.name + "is Running");
    }
};

// var smallMing = {name: 'lfk'};
//
// smallMing.__proto__ = Student;
//
// smallMing.run();

function createStudent(name) {
    var s = Object.create(Student);
    s.name = name;
    console.log(s.name);
    return s;
}

var smallMing = createStudent('lfkdks');


console.log(smallMing.name);
smallMing.run();