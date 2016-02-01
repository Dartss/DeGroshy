package com.future.degroshi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseIntArray;
import android.view.View;

public class DrawArcs extends View {
    Paint p;
    RectF rectf = new RectF(50, 1, 350, 300);
    SparseIntArray toDraw;

    protected DrawArcs(Context context, SparseIntArray mProcColor) {
        super(context);
        p = new Paint();
        this.toDraw = mProcColor;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        int mStartAngle = 0;
        float mSpaces = 320;

        for(int i=0; i <= toDraw.size(); i++){
            int mColor = toDraw.keyAt(i);
            int mAngel = toDraw.valueAt(i);

            p.setColor(mColor);

            canvas.drawArc(rectf, mStartAngle, mAngel, true, p);

            mStartAngle += mAngel;

            canvas.drawRect(20, mSpaces, 40, mSpaces + 20, p);

            for(Spent tmpSpnt : MainActivity.mSpents){
                if(tmpSpnt.mColor == mColor){
                    p.setColor(Color.BLACK);
                    p.setTextSize(20);

                    canvas.drawText(" - " + tmpSpnt.mName +
                            " (" + (100*mAngel)/360 + "%)",60,mSpaces+15,p);
                }
            }
            mSpaces += 40;
        }

    }

}
