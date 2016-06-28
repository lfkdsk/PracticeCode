//  autoStack.c
//  alTest
//
//  Created by 刘丰恺 on 16/6/28.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int key;
} element;

element *stack;
int size, top;

void createStack() {
    stack = (element *) malloc(sizeof(*stack));
    top = -1;
    size = 1;
}

void createStack(int maxSize) {
    int max = maxSize >= 1 ? maxSize : 1;
    top = -1;
    size = max;
    stack = (element *) malloc(size * sizeof(*stack));
}

bool isEmpty() {
    return top < 0;
}

bool isStackFull() {
    return top >= size - 1;
}

void stackFull() {
    element *temp = stack;
    stack = (element *) realloc(stack, 2 * size * sizeof(*stack));
    for (int i = 0; i < size; ++i) {
        stack[i] = temp[i];
        temp[i].key = -1;
    }
    size *= 2;
}

void push(element item) {
    top += 1;
    size += 1;
    if (isStackFull()) {
        stackFull();
    }
    stack[top] = item;
}

element pop() {
    if (isEmpty()) {
        element tempElement;
        tempElement.key = 0;
        return tempElement;
    } else {
        size -= 1;
        top -= 1;
        return stack[top];
    }
}

void printStack() {
    printf("size: %d - topElement: %d \n", size, stack[top].key);
    for (int i = 0; i < size; ++i) {
        printf("index: %d - key : %d \n", i, stack[i].key);
    }
}

int main() {
    createStack(5);

    element first, second, third, five;
    first.key = 1111;
    second.key = 2222;
    third.key = 3333;
    five.key = 4444;

    push(first);
    push(second);
    push(third);
    push(five);
    push(first);
    push(five);

//    pop();
//    pop();

    printStack();
}
