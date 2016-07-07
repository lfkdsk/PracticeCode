//
//  main.m
//  OCTrain
//
//  NS 标示此方法来源于Cocoa包而非其他
//  Created by 刘丰恺 on 16/7/7.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        // @标示字符串作为NSString处理
        NSLog(@"\n Hello, %s!","hentai");
        // BOOL类型由八位二进制数定义
        // 记住不要给BOOL赋数值
        if (YES) {
            NSLog(@"\n hahaha!");
        }
    }
    return 0;
}
