package bc.juhao.com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import bc.juhao.com.R;


public class TimerButton extends Button implements Runnable {

    private final static int DEFAULT_INTERVAL = 120;
    private final static int UPDATE_END = 0;
    private final static int UPDATE_DOING = 2;
    private int timer = 0;
    private int status = 0;

    public TimerButton(Context context) {
        super(context);
    }

    public TimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void start() {
        start(DEFAULT_INTERVAL);
    }


    public void end() {
        this.status = UPDATE_END;
        this.setText(R.string.timer_send_re);
        this.setEnabled(true);
    }

    private void update(int time) {
        this.setText(String.format(getHint().toString(), time));
    }


    public void prepare() {
        this.setText(R.string.timer_sending);
        this.setEnabled(false);
    }


    public void start(int interval) {
        if (status == UPDATE_DOING)
            return;
        this.status = UPDATE_DOING;
        this.setEnabled(false);
        this.timer = interval;
        run();
    }

    @Override
    public void run() {
        timer--;
        if (timer < 1) {
            end();
            return;
        }
        update(timer);
        postDelayed(this, 1000);
    }
}
