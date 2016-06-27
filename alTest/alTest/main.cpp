//
//  main.cpp
//  alTest
//
//  Created by 刘丰恺 on 16/6/27.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#include <iostream>

#define MALLOC(p,s) \
        if (!(p) = malloc(s) ){ \
            fprintf(stderr, "No Memory"); \
            exit(EXIT_FALURE); \
        }

#define CALLOC(p,n,s) \
        if (!((p) = calloc(n,s))) { \
            printf(stderr, "No Memory"); \
            exit(EXIT_FALURE); \
        }

#define REALLOC(p,s)\
    if (!((p) = realloc(p,s)){ \
        printf(stderr, "No Memory"); \
        exit(EXIT_FALURE); \
    }

int main(int argc, const char * argv[]) {

    return 0;
}
