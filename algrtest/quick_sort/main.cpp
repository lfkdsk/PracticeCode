//
//  main.cpp
//  quick_sort
//  快速排序
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#include <iostream>

int a[101],n;

void quick_sort(int left,int right){
    int i,j,t,temp;
    if (left > right) {
        return;
    }
    
    temp = a[left];
    i = left;
    j = right;
    while (i != j) {
        // 从右向左找出小于基准数的
        while (a[j] >= temp && i < j) {
            j--;
        }
        
        // 从左向右找大于基准数的
        while (a[i] <= temp && i < j) {
            i++;
        }
        
        if (i < j) {
            t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }
    
    a[left] = a[i];
    a[i] = temp;
    
    quick_sort(left, i-1);
    quick_sort(i+1, right);
}

int main(int argc, const char * argv[]) {
    int i,j;
    scanf("%d",&n);
    for (i = 0; i < n; i++) {
        scanf("%d",&a[i]);
    }
    
    quick_sort(0, n-1);
    
    for (j = 0; j < n; j++) {
        printf("%d ",a[j]);
    }
    return 0;
}
