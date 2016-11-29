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
import java.util.Map;

//class definition
public class CustomView extends View {

    private final int length_x = 10;
    private final int length_y = 10;

    private Paint black,gray,circleBlack,circleWhite;
    private List<Blockchain> blockchain;
    private List<int[][]> matrix;
    private Circle[][] board;

    private int step;
    private int tranche;

    private boolean touch;
    private float touchx;
    private float touchy;

    private int turn = 1;

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

        circleBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleBlack.setColor(Color.BLACK);
        circleWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleWhite.setColor(Color.WHITE);


    }

    public void onDraw(final Canvas canvas){
        //call the superclass method
        super.onDraw(canvas);

        // Largeur de la vue
        int width = getWidth();
        // Hauteur de la vue
        int height = getHeight();
        step = Math.min(width, height);

        tranche = step/11;

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
            canvas.drawCircle(board[j][k].getPosX(),board[j][k].getPosY(),board[j][k].getRadius(),board[j][k].getPaint());
            }

    }

    public boolean onTouchEvent(MotionEvent event) {


        // determine what kind of touch event we have

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){

            touch = true;
            touchx = event.getX();
            touchy = event.getY();

            placement();
            invalidate();
            return true;
            //}
        }

        if(event.getActionMasked()== MotionEvent.ACTION_UP){
            touch=false;
            return true;
        }

        return super.onTouchEvent(event);
    }

    public ArrayList<Circle> myNeigbhors(Circle c){
        ArrayList<Circle> result = new ArrayList<>();

        int x=getCoordMatrix(c).first;
        int y=getCoordMatrix(c).second;

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

        if(x==0&&y==0){
            result.add(board[x+1][y]);
            result.add(board[x][y+1]);
        }

        if(x==9&&y==0){
            result.add(board[x-1][y]);
            result.add(board[x][y+1]);
        }

        if(x==0&&y==9){
            result.add(board[x+1][y]);
            result.add(board[x][y-1]);
        }

        if(x==9&&y==9){
            result.add(board[x-1][y]);
            result.add(board[x][y-1]);
        }

        return result;
    }

    public Pair<Integer,Integer> getCoordMatrix(Circle circle){

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

    public Boolean searchFreeNeighbors(ArrayList<Circle> circleList){
        //return true if the chain has at least one free neighbors
        for(Circle c:circleList){
            for (Circle n:myNeigbhors(c)){
                if(n.getRadius()==0){
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean AmIAnEye(Circle c){
        //return true if a not colored circle as no empty neighbors and all of his neighbors are
        // colored in the same color
        //add the element in the eyeList
        int numberOfWhite=0;
        int numberOfBlack=0;
        if(c.getRadius()==0){

            for (Circle n:myNeigbhors(c)){
                if(n.getRadius()==0){
                    return false;
                }
                else if(n.getPaint()==circleBlack){
                    numberOfBlack++;
                }
                else if(n.getPaint()==circleWhite){
                    numberOfWhite++;
                }
            }
        }
        if(numberOfBlack==0||numberOfWhite==0){
            //add the element in the eyeList of the blockchain
            for (Blockchain b:blockchain){
                b.getEyeList().add(c);
                //attention on risque de creer des doublons
            }
            return true;
        }
        return false;
    }


    private boolean measureDistance(float ax, float bx, float ay, float by){
        float maximalDistance = tranche/2;//minimal distance between 2 circle /2

        return Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by)) < maximalDistance;
    }

    private void placement(){

        for(int j = 0; j<board.length;j++)
            for(int k = 0; k<board[j].length;k++) {
                if(measureDistance(touchx, board[j][k].getPosX(), touchy, board[j][k].getPosY())
                        && board[j][k].getRadius()==0){
                    //check regle ko
                    //check des yeux
                    //changement du circle
                    board[j][k].setRadius(tranche/2);
                    board[j][k].setColor(currentPaint());
                    //suppression des unités

                    removeSimpleCircle(board[j][k]);
                    addToBlockCHain(board[j][k]);
                    //suppression des groupes
                    //sauvegarde de la matrice actuelle dans une list
                    //fin du tour
                    turn = (turn + 1)%2;
                }
            }
    }

    private void addToBlockCHain(Circle circle) {
        for(Circle c : myNeigbhors(circle))
        {

        }
    }

    private Paint currentPaint(){
        //return paint of the current player
        if(turn%2==0){
            return circleWhite;

        }
        return circleBlack;

    }

    private void removeSimpleCircle(Circle c) {
        //on va chercher tous les voisins du jetons. Pour chaque voisins ennemis, on va vérifier si on peut l'enlever
        int opposant=0;

        for(Circle n : myNeigbhors(c)){

            if(n.getRadius()!=0 && n.getPaint()!=c.getPaint())
            {

                opposant=0;

                for(Circle m : myNeigbhors(n)){

                    if(n.getRadius()!=0 && m.getPaint()==c.getPaint()){
                        opposant++;
                        System.out.println(opposant);
                    }
                }
                if (opposant==myNeigbhors(n).size()){
                    n.setRadius(0);
                    n.setColor(gray);
                }
            }

        }

    }
}

