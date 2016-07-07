//
//  OCTrain
//
//  Created by 刘丰恺 on 16/7/7.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    FILE *wordFile = fopen("/Users/liufengkai/Documents/Projects/PracticeCode/OC/OCTrain/tmp/word.txt", "r");

    char word[100];

    while (fgets(word, 100, wordFile)) {
        word[strlen(word) - 1] = '\0';
        NSLog(@"%s is %d characters long", word, strlen(word));
    }

    fclose(wordFile);
    
    return 0;
}
