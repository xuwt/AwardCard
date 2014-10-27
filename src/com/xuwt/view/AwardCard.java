package com.xuwt.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xuwt.awardcard.R;

/**
 * 自定义View，刮刮乐
* @Description: TODO
* @author xuwt
* @date 2014年10月27日 下午5:22:19
* @version V1.0
 */
public class AwardCard extends View {
	
	/** 
	 * 控件的宽度 
	 */  
	private int mWidth;  
	/** 
	 * 控件的高度 
	 */  
	private int mHeight;
	
	/**
	 * 内存中创建的Canvas
	 */
	private Canvas mCanvas;
	
	/**
	 * mCanvas绘制内容在其上
	 */
	private Bitmap mBitmap;
	
	//图片资源
	private Bitmap mBitmapRes;
	
	/**
	 * 背景paint,灰色背景
	 */
	private Paint mBackPaint;
	
	//背景上面的文字内容paint
	private Paint mTextPaint;
	private Rect mTextBound;
	private String mText = "￥50000,00";
	
	//矩形边框
	private RectF mRectF;
	
	/**
	 * 记录用户绘制的Path
	 */
	private Path mPath;
	
	private int mLastX;
	private int mLastY;
	
	private boolean mIsInit;
	private boolean mIsComplete;

	
	public AwardCard(Context context) {
		this(context, null);
	}

	public AwardCard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AwardCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		mBackPaint = new Paint();
		
		// 设置画笔
		mBackPaint.setColor(Color.parseColor("#c0c0c0"));
		mBackPaint.setAntiAlias(true);
		mBackPaint.setDither(true);
		mBackPaint.setStyle(Paint.Style.STROKE);
		mBackPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
		mBackPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
		// 设置画笔宽度
		mBackPaint.setStrokeWidth(20);
		mBackPaint.setStyle(Paint.Style.FILL);
		
		
		mTextBound = new Rect();
		
		mTextPaint = new Paint();
		mTextPaint.setStyle(Style.FILL);
		mTextPaint.setTextScaleX(2f);
		mTextPaint.setColor(Color.DKGRAY);
		mTextPaint.setTextSize(32);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
		
		mBitmapRes=BitmapFactory.decodeResource(getResources(),
						R.drawable.award);
		
		mPath = new Path();
		
		//初始化draw
		mIsInit=true;
		mIsComplete=false;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		/**
		 * 设置宽度
		 */
		int specMode=MeasureSpec.getMode(widthMeasureSpec);
		int specSize=MeasureSpec.getSize(widthMeasureSpec);
		
		if(specMode==MeasureSpec.EXACTLY){
			mWidth=specSize;
		}else{
			
			//由图片决定的宽
			int desireByImg=getPaddingLeft()+getPaddingRight()+mBitmapRes.getWidth();
		    
			if(specMode==MeasureSpec.AT_MOST){
				mWidth=Math.min(desireByImg, specSize);
			}
			
		}
		
		/**
		 * 设置高度
		 */
		specMode=MeasureSpec.getMode(heightMeasureSpec);
		specSize=MeasureSpec.getSize(heightMeasureSpec);
		
		if(specMode==MeasureSpec.EXACTLY){
			mHeight=specSize;
		}else{
			
			//由图片决定的宽
			int desireByImg=getPaddingTop()+getPaddingBottom()+mBitmapRes.getHeight();
		    
			if(specMode==MeasureSpec.AT_MOST){
				mHeight=Math.min(desireByImg, specSize);
			}
			
		}
		
		setMeasuredDimension(mWidth, mHeight);
		
		mRectF=new RectF(0, 0, mWidth, mHeight);
		
		// 初始化bitmap
		mBitmap = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		
		mCanvas.drawRoundRect(mRectF, 30, 30, mBackPaint);
		mCanvas.drawBitmap(mBitmapRes, null, mRectF, null);
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		//画背景
		//canvas.drawBitmap(mBitmap, 0, 0, null);
		
		// 绘制奖项
		canvas.drawText(mText, mWidth / 2 - mTextBound.width() / 2,
				mHeight / 2 + mTextBound.height() / 2, mTextPaint);
		
		
		if (!mIsComplete)
		{
			
			mBackPaint.setStyle(Paint.Style.STROKE);
			//画线
			mBackPaint
					.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
			mCanvas.drawPath(mPath, mBackPaint);
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			mLastX = x;
			mLastY = y;
			mPath.moveTo(mLastX, mLastY);
			break;
		case MotionEvent.ACTION_MOVE:

			int dx = Math.abs(x - mLastX);
			int dy = Math.abs(y - mLastY);

			if (dx > 3 || dy > 3)
				mPath.lineTo(x, y);

			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
			new Thread(mRunnable).start();
			break;
		}

		invalidate();
		return true;
	}
	
	/**
	 * 统计擦除区域任务
	 */
	private Runnable mRunnable = new Runnable()
	{
		private int[] mPixels;

		@Override
		public void run()
		{

			int w = mWidth;
			int h = mHeight;

			float wipeArea = 0;
			float totalArea = w * h;

			Bitmap bitmap = mBitmap;

			mPixels = new int[w * h];

			/**
			 * 拿到所有的像素信息
			 */
			bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

			/**
			 * 遍历统计擦除的区域
			 */
			for (int i = 0; i < w; i++)
			{
				for (int j = 0; j < h; j++)
				{
					int index = i + j * w;
					if (mPixels[index] == 0)
					{
						wipeArea++;
					}
				}
			}

			/**
			 * 根据所占百分比，进行一些操作
			 */
			if (wipeArea > 0 && totalArea > 0)
			{
				int percent = (int) (wipeArea * 100 / totalArea);
				Log.e("TAG", percent + "");

				if (percent > 30)
				{
					//清除区域达到30%，下面自动清除
					mIsComplete = true;
					postInvalidate();
				}
			}
		}

	};

}
