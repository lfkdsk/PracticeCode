//
//  OCTrain
//
//  Created by 刘丰恺 on 16/7/7.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    const char *word[4] = {"aardvark", "abacus", "allude", "zygote"};
    int wordCount       = 4;
    int i;
    for (i = 0; i < wordCount; i++) {
        NSLog(@"%s is %d characters long", word[i], strlen(word[i]));
    }
    

    return 0;
}
