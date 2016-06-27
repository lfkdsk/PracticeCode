////
////  main.cpp
////  eval
////
////  Created by 刘丰恺 on 16/6/27.
////  Copyright © 2016年 刘丰恺. All rights reserved.
////
//
//#include <iostream>
//
//typedef enum {
//    lparen , rparen, plus, minus, times, divide,
//    mod, eos, operand
//} precedence;
//
//char* expr;
//
//precedence getToken(char *symbol, int *n){
//    *symbol = expr[(*n)++];
//    switch (*symbol) {
//        case '(':
//            return lparen;
//        case ')':
//            return rparen;
//        case '+':
//            return plus;
//        case '-':
//            return minus;
//        case '*':
//            return divide;
//        case '/':
//            return times;
//        case '%':
//            return mod;
//        case ' ':
//            return eos;
//        default:
//            return operand;
//    }
//}
//
//void eval(void){
//    precedence token;
//    char symbol;
//    int op1, op2;
//    int n = 0;
//    int top = -1;
//    token = getToken(&symbol, &n);
//    
//    while (token != eos) {
//        if (token == operand) {
//            push
//        }
//    }
//}
//
//int main(int argc, const char * argv[]) {
//    // insert code here...
//    std::cout << "Hello, World!\n";
//    return 0;
//}
