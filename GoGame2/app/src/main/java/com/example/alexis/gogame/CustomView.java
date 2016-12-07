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
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

//class definition
public class CustomView extends View {

    private Paint black,gray,circleBlack,circleWhite;
    private List<Blockchain> blockchain;
    private List<Circle[][]> matrix;
    private Circle[][] board;
    private List<Eye> eyes;
    private int whiteDead;
    private int blackDead;

    //private int step;
    private int tranche;

    //private boolean touch;
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
        board = new Circle[10][10];
        eyes = new ArrayList<>();


        gray = new Paint(Paint.ANTI_ALIAS_FLAG);
        gray.setColor(Color.TRANSPARENT);

        for(int i = 0;i<board.length;i++)
            for(int j = 0;j<board[i].length;j++)
                board[i][j] = new Circle(0,0,gray);


        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.STROKE);


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
        int step = Math.min(width, height);

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

            //touch = true;
            touchx = event.getX();
            touchy = event.getY();

            placement();
            invalidate();
            return true;
            //}
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

    public boolean searchFreeNeighbors(ArrayList<Circle> circleList){
        //return true if the chain has at least one free neighbors
        for(Circle c : circleList){
            for (Circle n : myNeigbhors(c)){
                if(n.getRadius()==0){
                    return true;
                }
            }
        }
        return false;
    }

    public void removeBlockchain(List<Blockchain> b){


        for (Blockchain element : blockchain)
        {
            if(b.contains(element)) {
                for (Circle m : element.getCircleList()) {
                    m.setRadius(0);
                    m.setColor(gray);
                    if (currentPaint() == circleWhite)
                        blackDead ++;
                    else
                        whiteDead ++;

                }
            }
        }

        blockchain.removeAll(b);
    }

    public List<Blockchain> findtoremove(Circle c)
    {
        //on va chercher tous les voisins du jetons. Pour chaque voisins ennemis, on va vérifier si
        //il appartient à une blockchain
        ArrayList<Blockchain> to_remove = new ArrayList<>();
        for(Circle n : myNeigbhors(c)){
            if(n.getRadius()!=0 && !n.getPaint().equals(c.getPaint()))
            {

                //cover all blockchain in the blockchain map
                //search the blockchain that contains circle c
                for(Blockchain b : blockchain){

                    if(b.contains(n)&&!searchFreeNeighbors(b.getCircleList())){
                        to_remove.add(b);
                    }
                }
            }
        }

        return to_remove;
    }


    public boolean AmIAnEye(Circle c) {

        boolean eyefound = false;
        Eye eye = new Eye(new Circle(0,0,gray),new ArrayList<Blockchain>());
        List<Eye> list = new ArrayList<>();
        for (Eye item : eyes) {
            list.add(item);
            if (item.getEye().equals(c) && !currentPaint().equals(item.getSurrounders().get(0).getCircleList().get(0).getPaint())) {
                eyefound = true;
                eye = new Eye(item);
            }
        }

        if (eyefound) {
            int tot_eye = 1;
            for(Eye element : list)
            {
                for(Blockchain block : element.getSurrounders())
                {
                    if(eye.getSurrounders().contains(block)) {
                        tot_eye++;
                    }
                }
            }
            tot_eye -= eye.getSurrounders().size();
            if (tot_eye > 1)
                return true;
            else {
                for (Blockchain element : eye.getSurrounders()) {
                    if(blockchainLiberties(element) == 1)
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    private void eyesUpdate(Circle c)
    {

        // map to store the the empty slot and the blockchains that surround it
        Map<Circle,List<Blockchain>> map = new HashMap<>();
        // loop over the neighbours of the selected point
        for(Circle circle : myNeigbhors(c))
        {
            // if the neighbour is an empty slot
            if(circle.getRadius() == 0)
            {
                // loop over the empty slot neighbours
                for(Circle neighbour : myNeigbhors(circle)) {
                    // if they are same color as the selected point
                    if(c.getPaint().equals(neighbour.getPaint())) {
                        // loop over the blockchain to find the blockchains
                        for (Blockchain element : blockchain) {
                            // we found a blockchain that contains an element around our empty slot
                            if (element.getCircleList().contains(neighbour))
                                // check if the point is not already in the map
                                if(map.containsKey(circle))
                                {
                                    List<Blockchain> value = map.get(circle);
                                    value.add(element);
                                }
                                // if not we create it
                                else
                                {
                                    List<Blockchain> value = new ArrayList<>();
                                    value.add(element);
                                    map.put(circle,value);
                                }
                        }
                    }
                    // if while looping over the neighbour we find an empty slot OR a different paint than the player who played
                    // we remove the circle from the map as it is not an empty slot surrounded by all same color
                    // we break to go out of the for loop
                    else
                    {
                        map.remove(circle);
                        break;
                    }
                }
            }
        }

        for(Map.Entry<Circle,List<Blockchain>> element : map.entrySet())
        {
            eyes.add(new Eye(element.getKey(),element.getValue()));
        }

        List<Eye> test = new ArrayList<>();
        for(Eye element : eyes)
            if(element.getEye().getRadius() != 0)
                test.add(element);
        eyes.removeAll(test);

    }


    private boolean measureDistance(float ax, float bx, float ay, float by){
        float maximalDistance = tranche/2;//minimal distance between 2 circle /2

        return Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by)) < maximalDistance;
    }

    private void placement(){

        for(int j = 0; j<board.length;j++)
            for(int k = 0; k<board[j].length;k++) {
                if(measureDistance(touchx, board[j][k].getPosX(), touchy, board[j][k].getPosY())
                        && board[j][k].getRadius()==0
                        &&!AmIAnEye(board[j][k])
                        && !koRule(j, k)){

                    //check regle ko

                    //changement du circle
                    board[j][k].setRadius(tranche/2);
                    board[j][k].setColor(currentPaint());
                    //suppression des unités
                    //suppression des groupes

                    removeBlockchain(findtoremove(board[j][k]));
                    //removeBlockchain(board[j][k]);
                    //mise a jour blockchain
                    addToBlockCHain(board[j][k]);
                    //sauvegarde de la matrice actuelle dans une list
                    matrix.add(copy_matrix(board));
                    //fin du tour
                    turn = (turn + 1)%2;
                    //check des yeux
                    eyesUpdate(board[j][k]);
                }
            }
    }

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

    private void addToBlockCHain(Circle circle) {

        boolean blockCreatedOrAdded = false;
        Blockchain current_blockchain = new Blockchain(new ArrayList<Circle>());
        ArrayList<Blockchain> blockchains_to_remove = new ArrayList<>();
        for(Circle c : myNeigbhors(circle))
        {
            if(c.getRadius() != 0 && c.getPaint().equals(circle.getPaint()))
            {
                for(Blockchain element : blockchain)
                {
                    if(element.contains(c) && !element.contains(circle) && !blockCreatedOrAdded)
                    {
                        element.getCircleList().add(circle);
                        current_blockchain = element;
                        blockCreatedOrAdded = true;
                    }
                    else if(element.contains(c) && !element.contains(circle) && blockCreatedOrAdded)
                    {
                        current_blockchain.getCircleList().addAll(element.getCircleList());
                        blockchains_to_remove.add(element);
                    }
                }
            }
        }
        if(!blockCreatedOrAdded)
        {
            ArrayList<Circle> newlist = new ArrayList<>();
            newlist.add(circle);
            blockchain.add(new Blockchain(newlist));

        }

        for(Blockchain to_remove : blockchains_to_remove)
            blockchain.remove(to_remove);

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

    private void countTerritories(){

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
        while(!notChangmentOccured){
            notChangmentOccured = true;

            for(Circle c : notBelongToTerritoriesCircle){
                for(Circle n : myNeigbhors(c)){
                    for (Circle b : whiteTestedCircle){
                        if(n.equals(b)){
                            addToNotBelongToTerritoriesCircle.add(b);
                            whiteTestedCircle.remove(b);
                        }
                    }
                    for (Circle b : blackTestedCircle){
                        if(n.equals(b)){
                            addToNotBelongToTerritoriesCircle.add(b);
                            blackTestedCircle.remove(b);
                        }
                    }
                    for (Circle b : waitingTestedCircle){
                        if(n.equals(b)){
                            addToNotBelongToTerritoriesCircle.add(b);
                            waitingTestedCircle.remove(b);
                        }
                    }
                    for(Circle element : addToNotBelongToTerritoriesCircle){
                        notBelongToTerritoriesCircle.add(element);
                        addToNotBelongToTerritoriesCircle.remove(element);
                    }
                }
            }
        }
    }
}


