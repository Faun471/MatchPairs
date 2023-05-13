package me.faun.matchpairs.customviews;

import android.animation.*;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import me.faun.matchpairs.R;

public class IgasPlayingCard extends AppCompatButton {
    private boolean isFlipped = false;
    private String name;
    private Drawable frontCard;
    private Drawable backCard;

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
            frontCard = a.getDrawable(R.styleable.IgasPlayingCard_switchOnImage);
            backCard = a.getDrawable(R.styleable.IgasPlayingCard_switchOffImage);
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
            frontCard = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
            backCard = ResourcesCompat.getDrawable(getResources(), R.drawable.back, null);
        }

        // set the initial drawable resource for the switch
        setBackground(backCard);
        setForeground(backCard);

        setOnClickListener(view -> flip());
    }

    public void flip() {
        isFlipped = !isFlipped;

        // create an ObjectAnimator for scaling the card
        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f, 1f)
        );
        scale.setDuration(250);
        scale.setInterpolator(new OvershootInterpolator());

        // create two ObjectAnimator instances for flipping the card
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f);
        ObjectAnimator flipIn = ObjectAnimator.ofFloat(this, "rotationY", -90f, 0f);
        flipIn.setDuration(200);
        flipOut.setDuration(200);

        // set the drawable resource for the switch after the flip animation ends
        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setForeground(isFlipped ? frontCard : backCard);
                flipIn.start();
            }
        });

        // play the animations together
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(scale, flipOut);
        animSet.playSequentially(flipOut, flipIn);
        animSet.start();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public Drawable getBackCard() {
        return backCard;
    }

    public Drawable getFrontCard() {
        return frontCard;
    }

    public void setBackCard(Drawable backCard) {
        this.backCard = backCard;
    }

    public void setFrontCard(String frontCard) {
        this.frontCard = ResourcesCompat.getDrawable(getResources(),
                switch (frontCard) {
                    case "abigail" -> R.drawable.abigail;
                    case "harvey" -> R.drawable.harvey;
                    case "elliott" -> R.drawable.elliott;
                    default -> R.drawable.ic_launcher_foreground;
                }, null);
    }
}