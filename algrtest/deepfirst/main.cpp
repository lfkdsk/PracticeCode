//
//  main.cpp
//  deepfirst
//  深度优先遍历
//  Created by 刘丰恺 on 16/5/12.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#include <stdio.h>

int min = 99999999 , book[101] ,n,e[101][101];

// cur 当前城市 dis 已走距离
void dfs(int cur,int dis){
    int j;
    
    if (dis > min) {
        return;
    }
    
    // 走了n 更新最短距离
    if (cur == n) {
        if (dis < min) {
            min = dis;
        }
        return;
    }
    
    for (j = 1; j <= n; j++) {
        
        if (e[cur][j] != 99999999 && book[j] == 0) {
            book[j] = 1;
            dfs(j, dis + e[cur][j]);
            book[j] = 0;
        }
    }
    return;
}

int main(){
    int i,j,m,a,b,c;
    
    scanf("%d %d",&n,&m);
    
    // init
    for (i = 1; i <= n; i++) {
        for (j = 1; j <= n; j++) {
            if (i == j) {
                e[i][j] = 0;
            }else{
                e[i][j] = 99999999;
            }
        }
    }
    
    for (i = 1; i <= m; i++) {
        scanf("%d %d %d",&a,&b,&c);
        e[a][b] = c;
    }
    
    // start from 1
    
    book[1] = 1;
    dfs(1, 0);// 1 , 0
    printf("%d",min);
    
    getchar();getchar();
    return 0;
}