//
//  OCTrain
//
//  Created by 刘丰恺 on 16/7/7.
//  Copyright © 2016年 刘丰恺. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    kCircle,
    kRectangle,
    kOblateSpheroid
} ShapeType;

typedef enum {
    kRedColor,
    kGreenColor,
    kBlueColor
} ShapeColor;

typedef struct {
    int x, y, width, heigh;
} ShapeRect;

typedef struct {
    ShapeType type;
    ShapeColor fillColor;
    ShapeRect bounds;
} Shape;


void drawShapes(Shape shapes[], int count){
    int i;
    
    for (int i = 0; i < count; i++) {
        NSLog(@"(%d, %d, %d, %d)",shapes[i].bounds.x,
              shapes[i].bounds.y,
              shapes[i].bounds.width,
              shapes[i].bounds.heigh);
    }
}


int main(int argc, const char * argv[]) {
    Shape shapes[1];
    
    ShapeRect rect0 = {0, 0, 10, 30};
    shapes[0].type = kCircle;
    shapes[0].fillColor = kRedColor;
    shapes[0].bounds = rect0;
    

    
    drawShapes(shapes, 1);
    
    return 0;
}

