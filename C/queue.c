
//  双向动态队列
//  Created by 刘丰恺 on 16/6/28.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int key;
} element;

element *queue;
int size, back;

void createQueue() {
    queue = (element *) malloc(sizeof(*queue));
    back = -1;
    size = 1;
}

void createQueue(int maxSize) {
    int max = maxSize >= 1 ? maxSize : 1;
    back = -1;
    size = max;
    queue = (element *) malloc(size * sizeof(*queue));
}

bool isEmpty() {
    return back < 0;
}

bool isQueueFull() {
    return back >= size - 1;
}

void queueFull() {
    element *temp = queue;
    queue = (element *) realloc(queue, 2 * size * sizeof(*queue));
    for (int i = 0; i < size; ++i) {
        queue[i] = temp[i];
        temp[i].key = -1;
    }
    size *= 2;
}

void pushBack(element item) {
    back += 1;
    size += 1;
    if (isQueueFull()) {
        queueFull();
    }
    queue[back] = item;
}

void push(element item) {
    back += 1;
    size += 1;
    if (isQueueFull()) {
        queueFull();
    }

    for (int i = size; i >= 0; --i) {
        queue[i] = queue[i + 1];
    }
    queue[0] = item;
}


element pop() {
    if (isEmpty()) {
        element tempElement;
        tempElement.key = 0;
        return tempElement;
    } else {
        back -= 1;
        element temp = queue[0];
        for (int i = 1; i < size; ++i) {
            queue[i - 1] = queue[i];
        }
        return temp;
    }
}

element popBack() {
    if (isEmpty()) {
        element tempElement;
        tempElement.key = 0;
        return tempElement;
    } else {
        back -= 1;
        return queue[back];
    }
}


void printStack() {
    printf("size: %d - topElement: %d - backElement: %d \n",
           size, queue[0].key, queue[back].key);
    for (int i = 0; i <= back; ++i) {
        printf("index: %d - key : %d \n", i, queue[i].key);
    }
}

int main() {
    createQueue(5);

    element first, second, third, five;
    first.key = 1111;
    second.key = 2222;
    third.key = 3333;
    five.key = 4444;

    pushBack(first);
    pushBack(second);
    pushBack(third);
    pushBack(five);
    pushBack(first);
    pushBack(five);

    printStack();


    pop();

    printStack();

    popBack();

    printStack();
}
