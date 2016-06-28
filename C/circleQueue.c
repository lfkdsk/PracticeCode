//  autoStack.c
//  alTest
//  单向动态队列
//  Created by 刘丰恺 on 16/6/28.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int key = -1;
} element;

element *queue;
int size, front, back, elementSize;

void createQueue(int maxSize) {
    int max = maxSize >= 1 ? maxSize : 1;
    back = 0;
    front = 0;
    elementSize = 0;
    size = max;
    queue = (element *) malloc(size * sizeof(*queue));
}

bool isEmpty() {
    return back < 0;
}

bool isQueueFull() {
    return ((back + 1) % size == front);
}

void queueFull() {
    queue = (element *) realloc(queue, 2 * size * sizeof(*queue));

    int distance = 0;
    int tempSize = size;
    size *= 2;
    // 这是判断了两个指针在中间的情况
    // 这说明队列出现了循环 所以要把其中的靠后的一部分 直接复制到
    // 数组的末尾
    if (abs(back - front) != elementSize) {
        if (back > front) {
            distance = tempSize - back;
            back = tempSize <= size ? back : size - distance;
        } else {
            distance = tempSize - front;
            front = tempSize <= size ? front : size - distance;
        }

        for (int i = 0; i < distance; ++i) {
            queue[size - i] = queue[tempSize - i];
        }
    }
}

void pushBack(element item) {
    if (isQueueFull()) {
        queueFull();
        queue[back++] = item;
    } else {
        queue[(back++) % size] = item;
    }
    elementSize++;
}


element pop() {
    if (isEmpty()) {
        element tempElement;
        tempElement.key = 0;
        return tempElement;
    } else {
        element temp = queue[(front) % size];
        queue[front].key = -1;
        front = (front == size ? 0 : front + 1) % size;
        elementSize--;
        return temp;
    }
}


void printStack() {
    printf("size: %d - topElement: %d - backElement: %d \n",
           size, queue[(front == size ? 0 : front + 1) % size].key,
           queue[back].key);
    printf("front: %d - back: %d \n", front, back);
    for (int i = 0; i < size; ++i) {
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

    for (int i = 0; i < 5; ++i) {
        pushBack(first);
    }
    printStack();


    for (int j = 0; j < 5; ++j) {
        pushBack(second);
    }

    printStack();

    for (int k = 0; k < 5; ++k) {
        pop();
        pushBack(first);
    }

    printStack();
}
