package com.example.alexis.gogame;

// imports
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

//class definition
public class CustomView extends View {

    Paint black;


    //default constructor
    public CustomView(Context c) {
        super(c);
        init();
    }

    //constructor that takes in a context and also a list of attributes
//that were set through XML
    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    // constructor that take in a context, attribute set and also a default
// style in case the view is to be styled in a certian way
    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    private int singleMeasure(int spec, int screenDim) {
        /*
          Mesure sur un axe

         */
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        // Si le layout n'a pas précisé de dimensions, la vue prendra la moitié de l'écran
        if(mode == MeasureSpec.UNSPECIFIED)
            return screenDim/2;
        else
            // Sinon, elle prendra la taille demandée par le layout
            return size;
    }


    @Override

    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        // On récupère les dimensions de l'écran
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        // Sa largeur…
        int screenWidth = metrics.widthPixels;
        // … et sa hauteur
        int screenHeight = metrics.heightPixels;
        int retourWidth = singleMeasure(widthMeasureSpec, screenWidth);
        int retourHeight = singleMeasure(heightMeasureSpec, screenHeight);
        // Comme on veut un carré, on n'aura qu'une taille pour les deux axes, la plus petite possible
        int retour = Math.min(retourWidth, retourHeight);
        setMeasuredDimension(retour, retour);
    }

    public void init() {
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.STROKE);
    }

    public void onDraw(final Canvas canvas){
        //call the superclass method
        super.onDraw(canvas);

        // Largeur de la vue
        int width = getWidth();
        // Hauteur de la vue
        int height = getHeight();
        int step = Math.min(width, height);

        float tranche = (float)step*0.1f;

        //canvas.drawRect(step * 0.125f, step * 0.125f, step * 0.875f, step * 0.875f, black);

        int i=0;
        while (i < 10){
            //horizontal
            canvas.drawLine(step * 0.125f+tranche*i, step * 0.125f,step * 0.125f+tranche*i, step * 0.875f, black);
            //vertical
            canvas.drawLine(step * 0.125f, step * 0.125f+tranche*i, step * 0.875f, step * 0.125f+tranche*i, black);
            i++;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
