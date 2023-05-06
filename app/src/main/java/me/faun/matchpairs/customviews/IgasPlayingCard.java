package me.faun.matchpairs.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageButton;
import me.faun.matchpairs.R;

public class IgasPlayingCard extends AppCompatImageButton {
    private boolean mIsOn = false;
    private String name;
    private int mSwitchOnImage;
    private int mSwitchOffImage;

    public IgasPlayingCard(Context context) {
        super(context);
        init(null);
    }

    public IgasPlayingCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public IgasPlayingCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IgasPlayingCard, 0, 0);
            mSwitchOnImage = a.getResourceId(R.styleable.IgasPlayingCard_switchOnImage, R.drawable.ic_launcher_foreground);
            mSwitchOffImage = a.getResourceId(R.styleable.IgasPlayingCard_switchOffImage, R.drawable.ic_launcher_background);
            a.recycle();
        } else {
            // default values
            mSwitchOnImage = R.drawable.ic_launcher_foreground;
            mSwitchOffImage = R.drawable.ic_launcher_background;
        }

        // set the initial image resource for the switch
        setImageResource(mSwitchOffImage);

        setOnClickListener(view -> toggle());
    }

    public void toggle() {
        mIsOn = !mIsOn;

        // create two ObjectAnimator instances for flipping the card
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f);
        ObjectAnimator flipIn = ObjectAnimator.ofFloat(this, "rotationY", -90f, 0f);

        // set the image resource for the switch after the flip animation ends
        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setImageResource(mIsOn ? mSwitchOnImage : mSwitchOffImage);
                flipIn.start();
            }
        });

        // play the flip animation
        AnimatorSet flip = new AnimatorSet();
        flip.playSequentially(flipOut, flipIn);
        flip.start();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
