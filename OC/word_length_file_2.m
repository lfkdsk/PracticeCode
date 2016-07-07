//
//  OCTrain
//
//  Created by 刘丰恺 on 16/7/7.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#import <Foundation/Foundation.h>
// 学到了 shift+command+< 为程序添加启动参数
int main(int argc, const char * argv[]) {
    
    if (argc == 1) {
        NSLog(@"you need provide a file name");
        return 1;
    }
    
    FILE *wordFile = fopen(argv[1], "r");

    char word[100];

    while (fgets(word, 100, wordFile)) {
        word[strlen(word) - 1] = '\0';
        NSLog(@"%s is %d characters long", word, strlen(word));
    }

    fclose(wordFile);
    
    return 0;
}
