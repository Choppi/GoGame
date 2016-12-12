package com.example.alexis.gogame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView black_timer;
    private TextView white_timer;
    private TextView tv_turn;
    private TextView white_pieces;
    private TextView white_territories;
    private TextView black_pieces;
    private TextView black_territories;
    private TextView black_score;
    private TextView white_score;
    private CustomView board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        white_timer = (TextView) findViewById(R.id.white_timer);
        white_pieces = (TextView) findViewById(R.id.white_pieces);
        white_territories = (TextView) findViewById(R.id.white_territories);
        white_score = (TextView) findViewById(R.id.white_score);

        black_timer = (TextView) findViewById(R.id.black_timer);
        black_pieces = (TextView) findViewById(R.id.black_pieces);
        black_territories = (TextView) findViewById(R.id.black_territories);
        black_score = (TextView) findViewById(R.id.black_score);

        tv_turn = (TextView) findViewById(R.id.tv_turn);
        board = (CustomView) findViewById(R.id.board_id);

        board.textview_setter(white_timer,white_pieces,white_territories,white_score,
                                black_timer,black_pieces,black_territories,black_score,
                                tv_turn);
        board.setTurn();

        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(900000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(900000)),
                TimeUnit.MILLISECONDS.toSeconds(900000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(900000)));
        white_timer.setText(hms);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reset:
                board.reset();
                return true;
            case R.id.finish:
                board.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
