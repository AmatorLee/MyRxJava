package com.amatorlee.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * 创建错左幅，包含
 * create工厂创建
 * just
 * fromArray
 * defer 此操作符直到有观察者订阅时才创建一个被观察者
 * repeat 重复发送数据队列
 * interval 递增发送integer
 * range 此操作符指定发送数据得个数
 * time 此操作符可以指定时间创建一个被观察者
 */
public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "AmatorLeeRxjava";
    private TextView mcreateTV;
    private StringBuilder mStringBuilder;
    private TextView mRangeTV;
    private TextView mTimeTV;
    private TextView mInterValTV;
    private TextView mRepeatTV;
    private TextView mDeferTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: " +  Thread.currentThread());
        initTextView();
        fromArray();
    }
    public void createRxJava(View view){
        createRxjava();
    }

    private void initTextView() {
        mStringBuilder = new StringBuilder();
        mcreateTV = (TextView) findViewById(R.id.createrx_tv);
        mRangeTV = (TextView) findViewById(R.id.range_tv);
        mTimeTV = (TextView) findViewById(R.id.time_tv);
        mInterValTV = (TextView) findViewById(R.id.interval_tv);
        mRepeatTV = (TextView) findViewById(R.id.repeat_tv);
        mDeferTV = (TextView) findViewById(R.id.defer_tv);
    }

    private void createRxjava() {
        //使用第一种create方法创建被观察者
        Flowable<String> observable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("hello,AmatorLee");
                e.onNext("使用第一种方法创建Rxjava");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        Subscriber<String> observer = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "onSubscribe");
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext" + s);
                mStringBuilder.append(s + '\n');
                mcreateTV.setText(mStringBuilder.toString());
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };

        observable.subscribe(observer);

        //方法二,使用just
        Flowable<String> observable2 = Flowable.just("AmatorLee", "正在使用just创建Rxjava");
              observable2  .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept: " + s);
                        mStringBuilder.append(s + '\n');
                        mcreateTV.setText(mStringBuilder.toString());
                    }
                });
        //方法三使用fromArray
        Flowable<String> observable3 = Flowable.fromArray("AmatorLee", "正在用第三种方法创建Rxjava");
        observable3.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mStringBuilder.append(s + '\n');
                mcreateTV.setText(mStringBuilder.toString());
            }
        });
    }
    public void range(View view){
        Flowable.range(1,5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept" + integer);
                mRangeTV.setText(integer.toString());
            }
        });
    }
    public void time(View view){
        Flowable.timer(1,TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(final Long aLong) throws Exception {
                        Log.i(TAG, "Time: " + aLong);
                        Log.i(TAG, "Time:" + Thread.currentThread());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTimeTV.setText(aLong.toString());
                            }
                        });
                    }
                });
    }
    public void interval(View view){
        Flowable.interval(1,2,TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(final Long aLong) throws Exception {
                Log.i(TAG, "Interval"  + Thread.currentThread());
                Log.i(TAG, "Interval " + aLong);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterValTV.setText(aLong.toString());
                    }
                });
            }
        });
    }
    public void repeat(View view){
        Flowable.just("Hello","AmatorLee").repeat(5).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "Repeat" + s);
                mRepeatTV.setText(s);
            }
        });
    }
    /*直到有调用Subscribe()方法才创建被观察者*/
    public void defer(View view){
        Flowable.defer(new Callable<Publisher<String>>() {
            @Override
            public Publisher<String> call() throws Exception {
                return Flowable.just("AmatorLee");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "Defer:" + s);
                mDeferTV.setText(s);
            }
        });
    }

    public void fromArray(){
        String[] items = {"1","2","3","4"};
        Flowable.fromArray(items).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "FromArray: " + s);
            }
        });
    }


}
