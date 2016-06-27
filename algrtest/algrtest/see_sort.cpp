//
//  see_sort.cpp
//  algrtest
//  冒泡排序
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#include <stdio.h>
int main(){
    int a[100],i,j,t,n;
    
    printf("请输入一个数据 ");
    scanf("%d",&n);
    
    for (i=0; i<n; i++) {
        printf("请输入一个数据 ");
        scanf("%d",&a[i]);
    }
    
    for (i = 0; i < n-1; i++) {
        for (j = 0; j < n-i; j++) {
            if (a[j] < a[j+1]) {
                t = a[j];
                a[j] = a[j+1];
                a[j+1] = t;
            }
        }
    }
    
    for (i=0; i < n; i++) {
        printf("%d ",a[i]);
    }
    getchar();
    return 0;
}