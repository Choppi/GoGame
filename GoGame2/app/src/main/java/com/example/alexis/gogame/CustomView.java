package com.example.alexis.gogame;

// imports
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

//class definition
public class CustomView extends View {

    private final int length_x = 10;
    private final int length_y = 10;

    private Paint black,gray;
    private List<Blockchain> blockchain;
    private List<int[][]> matrix;
    private Circle[][] board;

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
        blockchain = new ArrayList<>();
        matrix = new ArrayList<>();
        board = new Circle[length_x][length_y];
        for(int i = 0;i<board.length;i++)
            for(int j = 0;j<board[i].length;j++)
                board[i][j] = new Circle(0,0);


        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.STROKE);

        gray = new Paint(Paint.ANTI_ALIAS_FLAG);
        gray.setColor(Color.GRAY);
        gray.setStyle(Paint.Style.FILL);
    }

    public void onDraw(final Canvas canvas){
        //call the superclass method
        super.onDraw(canvas);

        // Largeur de la vue
        int width = getWidth();
        // Hauteur de la vue
        int height = getHeight();
        int step = Math.min(width, height);

        int tranche = step/11;

        int i=1;
        while (i < 11){
            //horizontal
            canvas.drawLine(i*tranche,tranche,i*tranche,10*tranche,black);
            //vertical
            canvas.drawLine(tranche,i*tranche,10*tranche,i*tranche,black);
            i++;
        }

        for(int j = 0; j<board.length;j++)
            for(int k = 0; k<board[j].length;k++) {
            board[j][k].setPosX((1+k)*tranche);
            board[j][k].setPosY((1+j)*tranche);
            canvas.drawCircle(board[j][k].getPosX(),board[j][k].getPosY(),board[j][k].getRadius(),gray);
            }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public ArrayList<Circle> myNeigbhors(){
        ArrayList<Circle> result = new ArrayList<>();

        int x=0;
        int y=0;

        if(x<9&&x>0&&y<9&&y>0){
            result.add(board[x-1][y]);
            result.add(board[x+1][y]);
            result.add(board[x][y-1]);
            result.add(board[x][y+1]);
        }

        if(y==0&&x>0&&x<9){
            result.add(board[x-1][y]);
            result.add(board[x+1][y]);
            result.add(board[x][y+1]);
        }

        if(y==9&&x>0&&x<9){
            result.add(board[x-1][y]);
            result.add(board[x+1][y]);
            result.add(board[x][y-1]);
        }

        if(x==9&&y>0&&y<9){
            result.add(board[x][y-1]);
            result.add(board[x][y+1]);
            result.add(board[x-1][y]);
        }

        if(x==0&&y>0&&y<9){
            result.add(board[x][y-1]);
            result.add(board[x][y+1]);
            result.add(board[x+1][y]);
        }


        return result;
    }

    public Pair<Integer,Integer> getCoordMatrix(Circle circle) throws Exception{

        for(int i = 0;i<board.length;i++)
        {
            for(int j = 0;j<board[i].length;j++)
            {
                 if(board[i][j].equals(circle))
                    return new Pair<>(i, j);
                 }
        }
        return null;
    }
}

