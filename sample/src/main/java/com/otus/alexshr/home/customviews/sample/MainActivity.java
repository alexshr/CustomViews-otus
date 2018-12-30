package com.otus.alexshr.home.customviews.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.otus.alexshr.home.customviews.CircleImageView;
import com.otus.alexshr.home.customviews.R;

public class MainActivity extends AppCompatActivity {

    boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView paddingTv = findViewById(R.id.paddingTv);
        TextView borderWidthTv = findViewById(R.id.borderWidthTv);
        TextView borderColorTv = findViewById(R.id.borderColorTv);
        TextView backgroundColorTv = findViewById(R.id.backgroundColorTv);
        TextView drawableTv = findViewById(R.id.drawableTv);
        CircleImageView circleView = findViewById(R.id.circleImageView);

        paddingTv.setOnClickListener(view -> {
            int padding = circleView.getPaddingLeft() == 0 ? 4 : 0;
            circleView.setPadding(padding, padding, padding, padding);
        });

        borderWidthTv.setOnClickListener(view -> {
            int borderWidth = circleView.getBorderWidth() == 2 ? 8 : 2;
            circleView.setBorderWidth(borderWidth);
        });

        borderColorTv.setOnClickListener(view -> {
            int color = circleView.getBorderColor() == Color.BLACK ? Color.RED : Color.BLACK;
            circleView.setBorderColor(color);
        });

        backgroundColorTv.setOnClickListener(view -> {
            int color = circleView.getBackgroundColor() == Color.TRANSPARENT ? Color.GREEN : Color.TRANSPARENT;
            circleView.setBackgroundColor(color);
        });

        drawableTv.setOnClickListener(view -> {
            circleView.setImageResource(b ? R.drawable.test_image1 : R.drawable.test_image2);
            b = !b;
        });
    }
}
