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
 * �Զ���View���ι���
* @Description: TODO
* @author xuwt
* @date 2014��10��27�� ����5:22:19
* @version V1.0
 */
public class AwardCard extends View {
	
	/** 
	 * �ؼ��Ŀ�� 
	 */  
	private int mWidth;  
	/** 
	 * �ؼ��ĸ߶� 
	 */  
	private int mHeight;
	
	/**
	 * �ڴ��д�����Canvas
	 */
	private Canvas mCanvas;
	
	/**
	 * mCanvas��������������
	 */
	private Bitmap mBitmap;
	
	//ͼƬ��Դ
	private Bitmap mBitmapRes;
	
	/**
	 * ����paint,��ɫ����
	 */
	private Paint mBackPaint;
	
	//�����������������paint
	private Paint mTextPaint;
	private Rect mTextBound;
	private String mText = "��50000,00";
	
	//���α߿�
	private RectF mRectF;
	
	/**
	 * ��¼�û����Ƶ�Path
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
		
		// ���û���
		mBackPaint.setColor(Color.parseColor("#c0c0c0"));
		mBackPaint.setAntiAlias(true);
		mBackPaint.setDither(true);
		mBackPaint.setStyle(Paint.Style.STROKE);
		mBackPaint.setStrokeJoin(Paint.Join.ROUND); // Բ��
		mBackPaint.setStrokeCap(Paint.Cap.ROUND); // Բ��
		// ���û��ʿ��
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
		
		//��ʼ��draw
		mIsInit=true;
		mIsComplete=false;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		/**
		 * ���ÿ��
		 */
		int specMode=MeasureSpec.getMode(widthMeasureSpec);
		int specSize=MeasureSpec.getSize(widthMeasureSpec);
		
		if(specMode==MeasureSpec.EXACTLY){
			mWidth=specSize;
		}else{
			
			//��ͼƬ�����Ŀ�
			int desireByImg=getPaddingLeft()+getPaddingRight()+mBitmapRes.getWidth();
		    
			if(specMode==MeasureSpec.AT_MOST){
				mWidth=Math.min(desireByImg, specSize);
			}
			
		}
		
		/**
		 * ���ø߶�
		 */
		specMode=MeasureSpec.getMode(heightMeasureSpec);
		specSize=MeasureSpec.getSize(heightMeasureSpec);
		
		if(specMode==MeasureSpec.EXACTLY){
			mHeight=specSize;
		}else{
			
			//��ͼƬ�����Ŀ�
			int desireByImg=getPaddingTop()+getPaddingBottom()+mBitmapRes.getHeight();
		    
			if(specMode==MeasureSpec.AT_MOST){
				mHeight=Math.min(desireByImg, specSize);
			}
			
		}
		
		setMeasuredDimension(mWidth, mHeight);
		
		mRectF=new RectF(0, 0, mWidth, mHeight);
		
		// ��ʼ��bitmap
		mBitmap = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		
		mCanvas.drawRoundRect(mRectF, 30, 30, mBackPaint);
		mCanvas.drawBitmap(mBitmapRes, null, mRectF, null);
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		//������
		//canvas.drawBitmap(mBitmap, 0, 0, null);
		
		// ���ƽ���
		canvas.drawText(mText, mWidth / 2 - mTextBound.width() / 2,
				mHeight / 2 + mTextBound.height() / 2, mTextPaint);
		
		
		if (!mIsComplete)
		{
			
			mBackPaint.setStyle(Paint.Style.STROKE);
			//����
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
	 * ͳ�Ʋ�����������
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
			 * �õ����е�������Ϣ
			 */
			bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

			/**
			 * ����ͳ�Ʋ���������
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
			 * ������ռ�ٷֱȣ�����һЩ����
			 */
			if (wipeArea > 0 && totalArea > 0)
			{
				int percent = (int) (wipeArea * 100 / totalArea);
				Log.e("TAG", percent + "");

				if (percent > 30)
				{
					//�������ﵽ30%�������Զ����
					mIsComplete = true;
					postInvalidate();
				}
			}
		}

	};

}
