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

//typedef struct {
//    ShapeType type;
//    ShapeColor fillColor;
//    ShapeRect bounds;
//} Shape;

@interface Shape : NSObject {
    ShapeColor fillColor;
    ShapeRect  bounds;
}

- (void) setFillColor: (ShapeColor) fillColor;

- (void) setBounds: (ShapeRect) bounds;

- (void) draw;

@end

@implementation Shape

- (void) setFillColor:(ShapeColor) fillColor {
    self->fillColor = fillColor;
}

- (void) setBounds:(ShapeRect) bound {
    bounds = bound;
}

- (void) draw {
    NSLog(@"drawing a circle at (%d %d %d %d)",
          bounds.x,bounds.y, bounds.width, bounds.heigh);
    NSLog(@"color %id", fillColor);
}

@end

/*
  继承类不必在声明实例变量
 */
 
@interface Trangle : Shape

@end

@implementation Trangle

- (void) setFillColor:(ShapeColor) color {
    if (color == kRedColor) {
        color = kGreenColor;
    }
    [super setFillColor:color];
}

- (void) draw {
    NSLog(@"trangle");
    NSLog(@"drawing a circle at (%d %d %d %d)",
          bounds.x,bounds.y, bounds.width, bounds.heigh);
    NSLog(@"color %id", fillColor);
}

@end

int main(int argc, const char * argv[]) {
    
    id shapes[1];
    ShapeRect rect0 = {0,0,10,30};
    shapes[0] = [Trangle new];
    [shapes[0] setBounds:rect0];
    [shapes[0] setFillColor:kRedColor];
    [shapes[0] draw];
    return 0;
}

