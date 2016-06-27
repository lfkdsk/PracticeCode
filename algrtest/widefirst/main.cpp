//
//  main.cpp
//  widefirst
//
//  Created by 刘丰恺 on 16/5/12.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#include <stdio.h>

struct note{
    int x; // 城市编号
    int s; // 转机次数
};

int main(){
    struct note que[2501];// 队列
    // 图
    int e[51][51] = {0},book[51] = {0};
    // 头／尾
    int head,tail;
    
    int i,j,n,m,a,b,cur,start,end,flag = 0;
    // 5个城市 ／ 七条航线 ／ 1 起点城市 ／ 5 目标城市
    scanf("%d %d %d %d",&n,&m,&start,&end);
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
    
    // init 7
    for (i = 1; i <= m; i++) {
        scanf("%d %d",&a,&b);
        // 无向图
        e[a][b] = 1;
        e[a][b] = 1;
    }
    
    head = 1;tail = 1;
    // 队尾start / 0
    que[tail].x = start;
    que[tail].s = 0;
    // ++
    tail++;
    
    book[1] = start;// start加入队列
    
    while (head < tail) {
        cur = que[head].x;// 队列首城市的编号
        for (j = 1; j <= n; j++) {
            // cur -> j 有飞机
            if (e[cur][j] != 99999999 && book[j] == 0) {
                // 入队
                que[tail].x = j;
                que[tail].s = que[head].s + 1;
                tail++;
                // 标记
                book[j] = 1;
            }
            
            // 到达目标城市
            if (que[tail - 1].x == end) {
                flag = 1;
                break;
            }
        }
        
        if (flag == 1) {
            break;
        }
        head++;// 继续扩展
    }
    
    // tail 是指向队列队尾的下一个位置
    printf("%d",que[tail - 1].s);
    
    getchar();getchar();
    return 0;
}
