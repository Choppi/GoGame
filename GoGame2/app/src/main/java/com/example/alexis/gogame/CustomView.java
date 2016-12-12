package com.example.alexis.gogame;

// imports
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

//class definition
public class CustomView extends View {

    private TextView white_timer;
    private TextView black_timer;
    private Paint black, gray, circleBlack, circleWhite;
    private List<Blockchain> blockchain;
    private List<Circle[][]> matrix;
    private Circle[][] board;
    private List<Eye> eyes;

    private long remainingTime_white = 900000;
    private long remainingTime_black = 900000;

    //private int step;
    private int tranche;

    public CountDownTimer white_countdown_timer;
    public CountDownTimer black_countdown_timer;

    //private boolean touch;
    private float touchx;
    private float touchy;


    private int numberOfBlackTerritories = 0;
    private int numberOfWhiteTerritories = 0;
    private int whiteDead = 0;
    private int blackDead = 0;

    private int turn = 1;

    private TextView tv_turn;
    private TextView white_pieces;
    private TextView white_territories;
    private TextView black_pieces;
    private TextView black_territories;
    private TextView black_score;
    private TextView white_score;

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
        if (mode == MeasureSpec.UNSPECIFIED)
            return screenDim / 2;
        else
            // Sinon, elle prendra la taille demandée par le layout
            return size;
    }


    @Override

    // TODO maxime comments
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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

    // TODO comment
    public void init() {
        blockchain = new ArrayList<>();
        matrix = new ArrayList<>();
        board = new Circle[10][10];
        eyes = new ArrayList<>();


        gray = new Paint(Paint.ANTI_ALIAS_FLAG);
        gray.setColor(Color.TRANSPARENT);

        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                board[i][j] = new Circle(0, 0, gray);


        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.STROKE);


        circleBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleBlack.setColor(Color.BLACK);
        circleWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleWhite.setColor(Color.WHITE);

        black_countdown_timer = new CountDownTimer(remainingTime_black, 1000) {
            public void onTick(long millisUntilFinished) {
                //update total with the remaining time left
                String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                remainingTime_black = millisUntilFinished;
                black_timer.setText(hms);

            }

            public void onFinish() {
                black_timer.setText("Time is up");

            }
        }.start();


    }

    // TODO comment
    public void onDraw(final Canvas canvas) {
        //call the superclass method
        super.onDraw(canvas);

        // Largeur de la vue
        int width = getWidth();
        // Hauteur de la vue
        int height = getHeight();
        int step = Math.min(width, height);

        tranche = step / 11;

        int i = 1;
        while (i < 11) {
            //horizontal
            canvas.drawLine(i * tranche, tranche, i * tranche, 10 * tranche, black);
            //vertical
            canvas.drawLine(tranche, i * tranche, 10 * tranche, i * tranche, black);
            i++;
        }

        for (int j = 0; j < board.length; j++)
            for (int k = 0; k < board[j].length; k++) {
                board[j][k].setPosX((1 + k) * tranche);
                board[j][k].setPosY((1 + j) * tranche);
                canvas.drawCircle(board[j][k].getPosX(), board[j][k].getPosY(), board[j][k].getRadius(), board[j][k].getPaint());
            }

    }

    // TODO Comment
    public boolean onTouchEvent(MotionEvent event) {
        // determine what kind of touch event we have

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

            touchx = event.getX();
            touchy = event.getY();

            placement();

            return true;
        }
        return super.onTouchEvent(event);
    }

    // TODO commment
    public ArrayList<Circle> myNeigbhors(Circle c) {
        ArrayList<Circle> result = new ArrayList<>();

        int x = getCoordMatrix(c).first;
        int y = getCoordMatrix(c).second;

        if (x < 9 && x > 0 && y < 9 && y > 0) {
            result.add(board[x - 1][y]);
            result.add(board[x + 1][y]);
            result.add(board[x][y - 1]);
            result.add(board[x][y + 1]);
        }

        if (y == 0 && x > 0 && x < 9) {
            result.add(board[x - 1][y]);
            result.add(board[x + 1][y]);
            result.add(board[x][y + 1]);
        }

        if (y == 9 && x > 0 && x < 9) {
            result.add(board[x - 1][y]);
            result.add(board[x + 1][y]);
            result.add(board[x][y - 1]);
        }

        if (x == 9 && y > 0 && y < 9) {
            result.add(board[x][y - 1]);
            result.add(board[x][y + 1]);
            result.add(board[x - 1][y]);
        }

        if (x == 0 && y > 0 && y < 9) {
            result.add(board[x][y - 1]);
            result.add(board[x][y + 1]);
            result.add(board[x + 1][y]);
        }

        if (x == 0 && y == 0) {
            result.add(board[x + 1][y]);
            result.add(board[x][y + 1]);
        }

        if (x == 9 && y == 0) {
            result.add(board[x - 1][y]);
            result.add(board[x][y + 1]);
        }

        if (x == 0 && y == 9) {
            result.add(board[x + 1][y]);
            result.add(board[x][y - 1]);
        }

        if (x == 9 && y == 9) {
            result.add(board[x - 1][y]);
            result.add(board[x][y - 1]);
        }

        return result;
    }

    // TODO comment && resolve null
    public Pair<Integer, Integer> getCoordMatrix(Circle circle) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(circle))
                    return new Pair<>(i, j);
            }
        }
        return null;
    }

    public boolean searchFreeNeighbors(ArrayList<Circle> circleList) {
        //return true if the chain has at least one free neighbors
        for (Circle c : circleList) {
            for (Circle n : myNeigbhors(c)) {
                if (n.getRadius() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO less complexity
    public void removeBlockchain(List<Blockchain> b) {


        for(Blockchain element : b)
        {
            for(Iterator<Eye> ite = eyes.iterator();ite.hasNext();)
            {
                Eye eye = ite.next();
                if(eye.getSurrounders().equals(element))
                {
                    ite.remove();
                }
            }
            for (Circle m : element.getCircleList()) {
                m.setRadius(0);
                m.setColor(gray);
                if (currentPaint().equals(circleBlack))
                    blackDead++;
                else
                    whiteDead++;
            }
        }

        blockchain.removeAll(b);

        /*
        for (Iterator<Blockchain> itb = blockchain.iterator();itb.hasNext();) {
            Blockchain element = itb.next();
            if (b.contains(element)) {
                for(Iterator<Eye> ite = eyes.iterator();ite.hasNext();)
                {
                    Eye eye = ite.next();
                    if(eye.getSurrounders().equals(element))
                    {
                        ite.remove();
                    }
                }
                for (Circle m : element.getCircleList()) {
                    m.setRadius(0);
                    m.setColor(gray);
                    if (currentPaint().equals(circleBlack))
                        blackDead++;
                    else
                        whiteDead++;
                }
                itb.remove();
            }
        }*/
    }

    // TODO comment && less complexity
    public List<Blockchain> findtoremove(Circle c) {
        //on va chercher tous les voisins du jetons. Pour chaque voisins ennemis, on va vérifier si
        //il appartient à une blockchain
        ArrayList<Blockchain> to_remove = new ArrayList<>();
        for (Circle n : myNeigbhors(c)) {
            if (n.getRadius() != 0 && !n.getPaint().equals(c.getPaint())) {
                //cover all blockchain in the blockchain map
                //search the blockchain that contains circle c
                for (Blockchain b : blockchain) {
                    if (b.contains(n) && !searchFreeNeighbors(b.getCircleList())) {
                        to_remove.add(b);
                    }
                }
            }
        }

        return to_remove;
    }

    // TODO comment
    public boolean AmIAnEye(Circle c) {

        boolean eyefound = false;
        Eye eye = null;
        List<Eye> list = new ArrayList<>();
        for (Eye item : eyes) {
            list.add(item);
            if (item.getEye().equals(c) && !currentPaint().equals(item.getSurrounders().getCircleList().get(0).getPaint())) {
                eyefound = true;
                eye = new Eye(item);
            }
        }

        if (eyefound) {
            int tot_eye = 0;
            for (Eye element : list) {
                if(element.getSurrounders().equals(eye.getSurrounders()))
                    tot_eye++;
                }
            if (tot_eye > 1)
                return true;
            else {
                if(blockchainLiberties(eye.getSurrounders()) == 1) {
                    return false;
                    }
                }

            }
        return false;
        }

    // TODO comment
    private void eyesUpdate(Circle c) {

        List<Circle> test = new ArrayList<>();
        Blockchain block_final = null;
        for(Blockchain b : blockchain)
        {
            if(b.getCircleList().contains(c))
            {
                for(Circle circle : b.getCircleList())
                {
                    for(Circle ccc : myNeigbhors(circle))
                    if(ccc.getRadius() == 0)
                        test.add(ccc);
                }
                block_final  = b;
                break;
            }
        }

        List<Circle> list_eye = new ArrayList<>();
        for(Circle cc : test)
        {
            if(Collections.frequency(test,cc) == myNeigbhors(cc).size() && !list_eye.contains(cc))
                list_eye.add(cc);
        }


        for(Circle value : list_eye)
        {
            eyes.add(new Eye(value,block_final));
        }

        for (Iterator<Eye> it = eyes.iterator();it.hasNext();) {
            Eye element = it.next();
            if (element.getEye().getRadius() != 0) {
                it.remove();
            }

        }
    }

    // TODO comment
    private boolean measureDistance(float ax, float bx, float ay, float by){
        float maximalDistance = tranche/2;//minimal distance between 2 circle /2

        return Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by)) < maximalDistance;
    }

    // TODO comment && see what takes the most time when large blockchain
    private void placement(){

        for(int j = 0; j<board.length;j++)
            for(int k = 0; k<board[j].length;k++) {
                if(measureDistance(touchx, board[j][k].getPosX(), touchy, board[j][k].getPosY())
                        && board[j][k].getRadius()==0
                        &&!AmIAnEye(board[j][k])
                        && !koRule(j, k)
                        && !suicide(board[j][k])){

                    //check regle ko

                    //changement du circle
                    board[j][k].setRadius(tranche/2);
                    board[j][k].setColor(currentPaint());
                    //suppression des unités
                    //suppression des groupes

                    invalidate();
                    removeBlockchain(findtoremove(board[j][k]));
                    turn = (turn + 1)%2;

                    UpdateTimer();
                    setTurn();
                    UpdateTextView();
                    invalidate();
                    //mise a jour blockchain
                    //sauvegarde de la matrice actuelle dans une list
                    matrix.add(copy_matrix(board));
                    //fin du tour

                    addToBlockCHain(board[j][k]);
                    //countTerritories();

                    //check des yeux
                    eyesUpdate(board[j][k]);

                }
            }
    }
    // TODO comment
    public void setTurn() {


        String white_string = getResources().getString(R.string.white);
        String black_string = getResources().getString(R.string.black);

        SpannableString whiteSpannable = new SpannableString(white_string);
        SpannableString blackSpannable = new SpannableString(black_string);

        whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, white_string.length(), 0);
        blackSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, black_string.length(), 0);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if(currentPaint().equals(circleBlack))
        {
            builder.append(blackSpannable);
            builder.append(getResources().getString(R.string.insert));
            tv_turn.setText(builder,TextView.BufferType.SPANNABLE);

        }
        else
        {
            builder.append(whiteSpannable);
            builder.append(getResources().getString(R.string.insert));
            tv_turn.setText(builder,TextView.BufferType.SPANNABLE);

        }
    }

    // TODO comment
    public void UpdateTimer() {
        if (currentPaint().equals(circleBlack))
        {
            white_countdown_timer.cancel();
            black_countdown_timer = new CountDownTimer(remainingTime_black, 1000) {
            public void onTick(long millisUntilFinished) {
                //update total with the remaining time left
                String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                remainingTime_black = millisUntilFinished;
                black_timer.setText(hms);

            }

            public void onFinish() {
                black_timer.setText("Time is up");

            }
        }.start();
    }
        else {
            black_countdown_timer.cancel();
            white_countdown_timer= new CountDownTimer(remainingTime_white, 1000) {
                public void onTick(long millisUntilFinished) {
                    //update total with the remaining time left
                    String hms = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        remainingTime_white = millisUntilFinished;
                        white_timer.setText(hms);

                }
                public void onFinish() {
                    white_timer.setText("Time is up");

                }
            }.start();
        }

    }

    // TODO comment
    private boolean suicide(Circle circle)
    {
        for(Circle c : myNeigbhors(circle))
        {
            if(c.getRadius() == 0)
                return false;
            else if(c.getPaint().equals(currentPaint()))
            {
                for(Blockchain b : blockchain) {
                    if(b.getCircleList().contains(c) && blockchainLiberties(b) > 1)
                        return false;
                }
            }
            else if (!c.getPaint().equals(currentPaint()))
            {
                for(Blockchain b : blockchain) {
                    if(b.getCircleList().contains(c) && blockchainLiberties(b) == 1)
                        return false;
                }
            }
        }


        return true;
    }

    // TODO comment && complexity ?
    private boolean koRule(int j, int k) {

        board[j][k].setRadius(tranche/2);
        board[j][k].setColor(currentPaint());
        Circle [][] tmp = copy_matrix(board);
        //suppression des unités
        //suppression des groupes
        List<Blockchain> values = findtoremove(board[j][k]);

        for(Blockchain element : values)
        {
            for(Circle c : element.getCircleList())
            {
                Pair<Integer,Integer> coord = getCoordMatrix(c);
                tmp[coord.first][coord.second].setRadius(0);
                tmp[coord.first][coord.second].setColor(gray);
            }
        }

        board[j][k].setRadius(0);
        board[j][k].setColor(gray);


        for(Circle[][] arrays : matrix)
        {
            if(matrix_equals(arrays,tmp)) {
                return true;
            }
        }

        return false;
    }

    // TODO comment && complexity ?
    private void addToBlockCHain(Circle circle) {

        ArrayList<Circle> newlist = new ArrayList<>();
        newlist.add(circle);
        Blockchain current_blockchain = new Blockchain(newlist);

        for(Circle c : myNeigbhors(circle))
        {
            if(c.getRadius() != 0 && c.getPaint().equals(circle.getPaint()))
            {
                for(Iterator<Blockchain> itb = blockchain.iterator();itb.hasNext();)
                {
                    Blockchain element = itb.next();
                    if(element.contains(c) && !element.contains(circle))
                    {
                        current_blockchain.getCircleList().addAll(element.getCircleList());
                        itb.remove();
                    }
                }
            }
        }
        blockchain.add(current_blockchain);
    }

    private Paint currentPaint(){
        //return paint of the current player
        if(turn%2==0){
            return circleWhite;
        }
        return circleBlack;
    }

    public int blockchainLiberties(Blockchain to_test) {
        List<Circle> all_liberties = new ArrayList<>();
        for(Circle circle : to_test.getCircleList())
        {
            for(Circle empty_circle : myNeigbhors(circle))
            {
                if(empty_circle.getRadius() == 0 && !all_liberties.contains(empty_circle))
                    all_liberties.add(empty_circle);
            }
        }

        return all_liberties.size();
    }

    public Circle[][] copy_matrix(Circle[][] matrix)
    {
        Circle[][] new_matrix = new Circle[matrix.length][matrix.length];
        for(int i = 0;i<matrix.length;i++)
        {
            for(int j = 0; j< matrix[i].length;j++)
            {
                new_matrix[i][j] = new Circle(matrix[i][j]);

            }
        }

        return new_matrix;
    }

    public boolean matrix_equals(Circle[][] a, Circle[][] b)
    {
        //test matrix
        if(a.length != b.length)
            return false;
        else
        {
            for(int i = 0; i < a.length;i++)
            {
                for(int j = 0; j< a[i].length;j++)
                {
                    if(!a[i][j].equals(b[i][j]))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void UpdateTextView()
    {
        white_pieces.setText("" + whiteDead);
        black_pieces.setText("" + blackDead);
        white_territories.setText("" + numberOfWhiteTerritories);
        black_territories.setText("" + numberOfBlackTerritories);

        white_score.setText("" + (whiteDead + numberOfWhiteTerritories));
        black_score.setText("" + (blackDead + numberOfBlackTerritories));
    }

    public void textview_setter(TextView white_timer,
                                TextView white_pieces,
                                TextView white_territories,
                                TextView white_score,
                                TextView black_timer,
                                TextView black_pieces,
                                TextView black_territories,
                                TextView black_score,
                                TextView tv_turn)
    {
        this.white_timer = white_timer;
        this.white_pieces = white_pieces;
        this.white_territories = white_territories;
        this.white_score = white_score;
        this.black_timer = black_timer;
        this.black_pieces = black_pieces;
        this.black_territories = black_territories;
        this.black_score = black_score;
        this.tv_turn = tv_turn;
    }

    // TODO implementation
    public void reset()
    {
        System.out.println("Reset");
    }

    // TODO implementation
    public void finish() {
        System.out.println("Finish");
    }

/*    private void countTerritories(){

        List<Circle> waitingTestedCircle = new ArrayList<Circle>();
        List<Circle> blackTestedCircle = new ArrayList<Circle>();
        List<Circle> whiteTestedCircle = new ArrayList<Circle>();
        List<Circle> notBelongToTerritoriesCircle = new ArrayList<Circle>();

        for(int j = 0; j<board.length;j++)
            for(int k = 0; k<board[j].length;k++) {

                if(board[j][k].getPaint()==gray){
                    int numberOfWhiteNeighbors = 0;
                    int numberOfBlackNeighbors = 0;

                    for(Circle c : myNeigbhors(board[j][k])){
                        if(c.getPaint()==circleWhite)
                            numberOfWhiteNeighbors++;
                        else if(c.getPaint()==circleBlack)
                            numberOfBlackNeighbors++;
                    }
                    if (numberOfBlackNeighbors > 0 && numberOfWhiteNeighbors > 0)
                        notBelongToTerritoriesCircle.add(board[j][k]);
                    else if (numberOfBlackNeighbors > 0 && numberOfWhiteNeighbors == 0)
                        blackTestedCircle.add(board[j][k]);
                    else if (numberOfBlackNeighbors == 0 && numberOfWhiteNeighbors > 0)
                        whiteTestedCircle.add(board[j][k]);
                    else
                        waitingTestedCircle.add(board[j][k]);
                }
            }
        boolean notChangmentOccured = false;
        List<Circle> addToNotBelongToTerritoriesCircle = new ArrayList<Circle>();
        List<Circle> addToWhiteTestedCircle = new ArrayList<Circle>();
        List<Circle> addToBlackTestedCircle = new ArrayList<Circle>();
        List<Circle> removeToBlackTestedCircle = new ArrayList<Circle>();
        List<Circle> removeToWhiteTestedCircle = new ArrayList<Circle>();
        List<Circle> removeTowaitingTestedCircle = new ArrayList<Circle>();

        while(!notChangmentOccured){
            notChangmentOccured = true;

            for(Circle c : notBelongToTerritoriesCircle){
                for(Circle n : myNeigbhors(c)){
                    for (Circle b : whiteTestedCircle){
                        if(this.getCoordMatrix(n).equals(this.getCoordMatrix(b))){
                            addToNotBelongToTerritoriesCircle.add(b);
                            removeToWhiteTestedCircle.add(b);
                            notChangmentOccured = false;
                        }
                    }
                    for (Circle b : blackTestedCircle){
                        if(this.getCoordMatrix(n).equals(this.getCoordMatrix(b))){
                            addToNotBelongToTerritoriesCircle.add(b);
                            //blackTestedCircle.remove(b);
                            removeToBlackTestedCircle.add(b);
                            notChangmentOccured = false;
                        }
                    }
                    for (Circle b : waitingTestedCircle){
                        if(this.getCoordMatrix(n).equals(this.getCoordMatrix(b))){
                            addToNotBelongToTerritoriesCircle.add(b);
                            //waitingTestedCircle.remove(b);
                            removeTowaitingTestedCircle.add(b);
                            notChangmentOccured = false;
                        }
                    }
                }
            }
            blackTestedCircle.removeAll(removeToBlackTestedCircle);
            removeToBlackTestedCircle.clear();
            whiteTestedCircle.removeAll(removeToWhiteTestedCircle);
            removeToWhiteTestedCircle.clear();
            waitingTestedCircle.removeAll(removeTowaitingTestedCircle);
            removeTowaitingTestedCircle.clear();

            for ( Circle c : whiteTestedCircle){
                for(Circle n : myNeigbhors(c)){
                    for(Circle b : waitingTestedCircle){
                        if(this.getCoordMatrix(n).equals(this.getCoordMatrix(b))){
                            addToWhiteTestedCircle.add(b);
                            //waitingTestedCircle.remove(b);
                            removeTowaitingTestedCircle.add(b);
                            notChangmentOccured = false;
                        }
                    }
                    for(Circle b : blackTestedCircle){
                        if(this.getCoordMatrix(n).equals(this.getCoordMatrix(b))){
                            addToNotBelongToTerritoriesCircle.add(b);
                            addToNotBelongToTerritoriesCircle.add(c);
                            //whiteTestedCircle.remove(c);
                            //blackTestedCircle.remove(b);
                            removeToBlackTestedCircle.add(b);
                            removeToWhiteTestedCircle.add(c);
                            notChangmentOccured = false;
                        }
                    }
                }
            }

            blackTestedCircle.removeAll(removeToBlackTestedCircle);
            removeToBlackTestedCircle.clear();
            whiteTestedCircle.removeAll(removeToWhiteTestedCircle);
            removeToWhiteTestedCircle.clear();
            waitingTestedCircle.removeAll(removeTowaitingTestedCircle);
            removeTowaitingTestedCircle.clear();

            for ( Circle c : blackTestedCircle) {
                for (Circle n : myNeigbhors(c)) {
                    for (Circle b : waitingTestedCircle) {
                        if (this.getCoordMatrix(n).equals(this.getCoordMatrix(b))) {
                            addToBlackTestedCircle.add(b);
                            //waitingTestedCircle.remove(b);
                            removeTowaitingTestedCircle.add(b);
                            notChangmentOccured = false;
                        }
                    }
                    for (Circle b : whiteTestedCircle) {
                        if (this.getCoordMatrix(n).equals(this.getCoordMatrix(b))) {
                            addToNotBelongToTerritoriesCircle.add(b);
                            addToNotBelongToTerritoriesCircle.add(c);
                            //whiteTestedCircle.remove(b);
                            //blackTestedCircle.remove(c);
                            removeToBlackTestedCircle.add(c);
                            removeToWhiteTestedCircle.add(b);
                            notChangmentOccured = false;
                        }
                    }
                }
            }

            blackTestedCircle.removeAll(removeToBlackTestedCircle);
            removeToBlackTestedCircle.clear();
            whiteTestedCircle.removeAll(removeToWhiteTestedCircle);
            removeToWhiteTestedCircle.clear();
            waitingTestedCircle.removeAll(removeTowaitingTestedCircle);
            removeTowaitingTestedCircle.clear();

            for(Circle element : addToNotBelongToTerritoriesCircle){
                notBelongToTerritoriesCircle.add(element);
            }
            addToNotBelongToTerritoriesCircle.clear();

            for(Circle element : addToBlackTestedCircle){
                blackTestedCircle.add(element);
            }
            addToBlackTestedCircle.clear();

            for(Circle element : addToWhiteTestedCircle){
                whiteTestedCircle.add(element);
            }
            addToWhiteTestedCircle.clear();
        }

        numberOfWhiteTerritories=whiteTestedCircle.size();
        numberOfBlackTerritories=blackTestedCircle.size();
        System.out.println("number of black terri "+numberOfBlackTerritories);
        System.out.println("number of white terri "+numberOfWhiteTerritories);
    }
*/

}


