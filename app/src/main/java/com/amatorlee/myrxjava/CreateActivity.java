package com.amatorlee.myrxjava;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 创建操作符，包括但不限：
 * create
 * just
 * fromArray
 * time
 * defer
 * repeat
 * interval
 * range
 */
public class CreateActivity extends AppCompatActivity {

    private StringBuilder mStringBuilder;
    private TextView mCreateTextView;
    private TextView mJustTextView;
    private TextView mFromArrayTextView;
    private TextView mTimeTextView;
    private TextView mDeferTextView;
    private TextView mInterValTextView;
    private TextView mRepeatTextView;
    private TextView mRangeTextView;
    private static final String TAG = "CreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initTextView();
    }

    private void initTextView() {
        mCreateTextView = (TextView) findViewById(R.id.create_tv);
        mJustTextView = (TextView) findViewById(R.id.just_tv);
        mFromArrayTextView = (TextView) findViewById(R.id.fromArray_tv);
        mTimeTextView = (TextView) findViewById(R.id.time_tv);
        mDeferTextView = (TextView) findViewById(R.id.defer_tv);
        mInterValTextView = (TextView) findViewById(R.id.interval_tv);
        mRepeatTextView = (TextView) findViewById(R.id.repeat_tv);
        mRangeTextView = (TextView) findViewById(R.id.range_tv);
    }

    public void create(View view) {
        mStringBuilder = new StringBuilder();
        //被观察者
        Flowable<String> observable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("AmatorLee");
                e.onNext("正在用create操作符创建被观察者");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
        //观察者
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                /*
                此方法用以发送请求次数如不指定则不发送，一般指定为integer.MAX_VALUE
                 */
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                mStringBuilder.append(s + "\n");
                Log.i(TAG, "onNext:" + s);
                mCreateTextView.setText(mStringBuilder.toString());
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "onError Excute");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete excute ");
            }
        };
        //绑定
        observable.subscribe(subscriber);
    }
    public void just(View view){
        mStringBuilder = new StringBuilder();
        Flowable.just("AmatorLee","利用just操作符创建被观察者").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mStringBuilder.append(s + "\n");
                Log.i(TAG, "just:" + s);
                mJustTextView.setText(mStringBuilder.toString());

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "onError:" + throwable.getMessage());
            }
        });
    }
    public void fromArray(View view){
        /**
         *此操作符等作用是逐个发送数据队列，而just则是一并发送
         */
        mStringBuilder = new StringBuilder();
        String[] item={"AmatorLee","正在使用fromArray操作符创建被观察者"};
        Flowable.fromArray(item).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "fromArray:" + s);
                mStringBuilder.append(s + "\n");
                mFromArrayTextView.setText(mStringBuilder.toString());
            }
        });
    }
    public void time(View view){
        /**
         * 此操作符主要是指定延时时间发送一个数据为0的被观察者
         * 另外此被观察者发送在默认线程，如操作UI需返回UI线程
         * 1.通过三个参数的构造方法指定
         * 2.通过subscribeOn()方法指定
         * 3.通过Handler，runOnMainThgread()方法等
         */
        Flowable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i(TAG, "time:" + aLong);
                mTimeTextView.setText(aLong + "");
            }
        });
    }
    public void defer(View view){
        /**
         * 此操作符等待有订阅关系发生才发送数据
         */
        mStringBuilder = new StringBuilder("AmatorLee");
        Flowable<String> flowable = Flowable.defer(new Callable<Publisher<String>>() {
            @Override
            public Publisher<String> call() throws Exception {
                return Flowable.just(mStringBuilder.toString());
            }
        });
        mStringBuilder.append("\n" + "利用defer操作符创建被观察者");
        flowable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "defer:" + s);
                mDeferTextView.setText(s);
            }
        });
    }
    public void interval(View view){
        /**
         * 此操作符会发送一个不断递增的数值
         */
        mStringBuilder = new StringBuilder();
        Flowable f = Flowable.interval(1, TimeUnit.SECONDS,AndroidSchedulers.mainThread());
        Consumer<Long> consumer = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i(TAG, "interval:" + aLong);
                mStringBuilder.append(aLong + " ");
                mInterValTextView.setText(mStringBuilder.toString());
            }
        };
         f.subscribe(consumer);
    }
    public void repeat(View view){
        /**
         * 此操作符可以指定重复发送某个数据序列
         * 如调用的是无参的构造方法，默认为integer.MAX_VALUE
         */
        mStringBuilder = new StringBuilder();
        Flowable.just("利用repeat操作符创建被观察者").repeat(3).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "repeat:" + s);
                mStringBuilder.append(s + "\n");
                mRepeatTextView.setText(mStringBuilder.toString());
            }
        });
    }
    public void range(View view){
        /**
         * 此操作符可以指定开始的数值与总数值进行数数
         */
        mStringBuilder = new StringBuilder();
        Flowable.range(1,5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "range:" + integer);
                mStringBuilder.append(integer + "\n");
                mRangeTextView.setText(mStringBuilder.toString());
            }
        });
    }
}
