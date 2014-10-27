AwardCard
=========

刮刮卡简单实现

```xml
android.graphics.Paint
public Xfermode setXfermode (Xfermode xfermode)
Set or clear the xfermode object. Pass null to clear any previous xfermode. 
As a convenience, the parameter passed is also returned.
```

设置两张图片相交时的模式。
在正常的情况下，在已有的图像上绘图将会在其上面添加一层新的形状。如果新的Paint是完全不透明的，那么它将完全遮挡住下面的Paint；如果它是部分透明的，那么它将会被染上下面的颜色。
而setXfermode就可以来解决这个问题 .

```java
Canvas canvas = new Canvas(dstBitmap);  
paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));    
canvas.drawBitmap(srcBitmap, 0f, 0f, paint);   
```

canvas原有的图片可以理解为背景，就是dst；
新画上去的图片可以理解为前景，就是src。
![](https://github.com/xuwt/AwardCard/raw/master/image/mode.gif)  