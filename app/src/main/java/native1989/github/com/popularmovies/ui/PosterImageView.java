package native1989.github.com.popularmovies.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public final class PosterImageView extends ImageView {
    public PosterImageView(Context context) {
        super(context);
    }

    public PosterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth() * 278 / 185);
    }
}