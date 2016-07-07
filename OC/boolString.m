//
//  main.m
//  OCTrain
//
//  Created by 刘丰恺 on 16/7/7.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#import <Foundation/Foundation.h>

BOOL areIntDifferent(int thing1, int thing2){
    if (thing1 == thing2) {
        return (NO);
    } else {
        return (YES);
    }
}

NSString *boolString(BOOL yesNo){
    if (yesNo == NO) {
        return @"no";
    } else {
        return @"yes";
    }
}


int main(int argc, const char * argv[]) {
    @autoreleasepool {
        BOOL areTheyDifferent;
        
        areTheyDifferent = areIntDifferent(5, 5);
        // 此处的％@标示是NSString类型的指针
        NSLog(@"are %d and %d different? %@",5 ,5, boolString(areTheyDifferent));
        
        areTheyDifferent = areIntDifferent(22, 34);
        
        NSLog(@"are %d and %d different? %@",5 ,5, boolString(areTheyDifferent));
        
    }
    return 0;
}
