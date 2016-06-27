/**
 * 模板引擎
 * @param html 传入文本
 * @param options 参数
 * @returns 返回是个方法
 * @constructor
 */
var TemplateEngine = function (html, options) {
    // 匹配正则式标记 /g是批量替换
    var re = /<%([^%>]+)?%>/g;
    // 这段正则是用来匹配高级方法的
    var reExp = /(^( )?(if|for|else|switch|case|break|{|}))(.*)?/g;
    var code = 'var r = [];\n';
    var cursor = 0;
    var match;
    var add = function (line, js) {
        // 如果是代码直接添加 否则通过push进入数组
        // 下一行实在排除调用加 " "
        js ? (code += line.match(reExp) ? line + '\n' : 'r.push(' + line + ');\n') :
            (code += line != '' ? 'r.push("' + line.replace(/"/g, '\\"') + '");\n' : '');
        return add;
    };
    while (match = re.exec(html)) {
        console.log('match ' + match);
        // first 其实是在拿两个模版标签的中间部分
        var first = html.slice(cursor, match.index);
        // console.log("first" + first);
        // 这才是匹配
        var second = match[1];
        // console.log("second" + second);
        add(first)(second, true);
        // 更新了cursor位置
        cursor = match.index + match[0].length;
    }
    add(html.substr(cursor, html.length - cursor));
    // 为函数添加返回语句
    code += 'return r.join("");';
    //
    return new Function(code.replace(/[\r\t\n]/g, '')).apply(options);
};

console.log(TemplateEngine('My skills:' +
    '<%if(this.showSkills) {%>' +
    '<%for(var index in this.skills) {%>' +
    '<a href="#"><%this.skills[index]%></a>' +
    '<%}%>' +
    '<%} else {%>' +
    '<p>none</p>' +
    '<%}%>', {
    showSkills: true,
    skills: ['fuck', 'shit', 'hahaha']
}));