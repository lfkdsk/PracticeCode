//
//  main.cpp
//  Graph
//
//  Created by 刘丰恺 on 16/5/12.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#include <stdio.h>
int book[101] , sum , n, e[101][101];

/*cur 当前节点的定点编号*/
void dfs(int cur){
    int i;
    printf("%d \t",cur);
    
    sum++;// 每访问一个节点sum自加1
    if (sum == n) {
        return;
    }
    
    for (i = 0; i < n; i++) {
        if (e[cur][i] == 1 && book[i] == 0) {
            book[i] = 1;
            dfs(i);
        }
    }
    
    return;
}


int main(){
    int i,j,m,a,b;
    
    scanf("%d %d" , &n,&m);
    
    for (i = 0; i < n;i++) {
        for (j = 0; j < n; j++) {
            if (i == j) {
                e[i][j] = 0;
            }else {
                e[i][j] = 999999999;
            }
        }
    }
    
    for (i = 0 ; i < m; i++) {
        scanf("%d %d",&a,&b);
        e[a][b] = 1;
        e[a][b] = 1;// 无向图
    }
    
    book[0] = 1;
    dfs(0);
    
    getchar();getchar();
    return 0;
}