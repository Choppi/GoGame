package com.example.alexis.gogame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView black_timer;
    private TextView white_timer;
    private TextView tv_turn;
    private CustomView board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        black_timer = (TextView) findViewById(R.id.black_timer);
        white_timer = (TextView) findViewById(R.id.white_timer);
        tv_turn = (TextView) findViewById(R.id.tv_turn);
        board = (CustomView) findViewById(R.id.board_id);

        board.textview_setter(white_timer,black_timer,tv_turn);
        board.setTurn();

        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(900000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(900000)),
                TimeUnit.MILLISECONDS.toSeconds(900000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(900000)));
        white_timer.setText(hms);

    }
}
