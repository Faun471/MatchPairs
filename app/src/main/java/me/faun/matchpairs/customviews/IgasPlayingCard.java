package me.faun.matchpairs.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import me.faun.matchpairs.R;

public class IgasPlayingCard extends AppCompatButton {
    private boolean mIsOn = false;
    private String name;
    private Drawable mSwitchOnImage;
    private Drawable mSwitchOffImage;

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
            mSwitchOnImage = a.getDrawable(R.styleable.IgasPlayingCard_switchOnImage);
            mSwitchOffImage = a.getDrawable(R.styleable.IgasPlayingCard_switchOffImage);
            // read corner radius attribute, default to 0
            float cornerRadius = a.getDimension(R.styleable.IgasPlayingCard_cornerRadius, 0);
            a.recycle();

            // create background drawable with rounded corners
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(cornerRadius);
            setBackground(background);
        } else {
            // default values
            mSwitchOnImage = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
            mSwitchOffImage = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_background, null);
        }

        // set the initial drawable resource for the switch
        setForeground(mSwitchOffImage);

        setOnClickListener(view -> flip());
    }


    public void flip() {
        mIsOn = !mIsOn;

        // create two ObjectAnimator instances for flipping the card
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f);
        ObjectAnimator flipIn = ObjectAnimator.ofFloat(this, "rotationY", -90f, 0f);

        // set the drawable resource for the switch after the flip animation ends
        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setForeground(mIsOn ? mSwitchOnImage : mSwitchOffImage);
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