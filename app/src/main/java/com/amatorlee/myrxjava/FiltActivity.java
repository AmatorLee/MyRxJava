package com.amatorlee.myrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/***
 * 过滤操作符包含但不限于
 * filter按条件过滤
 * distinct 过滤重复数据
 * elementAt 过滤指定下标的数据
 * first 过滤掉第一项
 * last 过滤最后一项数据
 * take 只取几项数据其他活驴
 * skip 过滤指定下边前几项数据
 * doubance 过滤掉发送频率过快的数据
 */
public class FiltActivity extends AppCompatActivity {

    private StringBuilder mStringBuilder;
    private static final String TAG = "FiltActivity";
    private TextView mDistinctTV;
    private TextView mElementAtTV;
    private TextView mFirstTV;
    private TextView mLastTV;
    private TextView mTakeTV;
    private TextView mSkipTextView;
    private TextView mFiltTextView;
    private TextView mDoubanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filt);
        initTextView();
    }

    private void initTextView() {
        mDistinctTV = (TextView) findViewById(R.id.distinct_tv);
        mElementAtTV = (TextView) findViewById(R.id.elementAt_tv);
        mFirstTV = (TextView) findViewById(R.id.first_tv);
        mLastTV = (TextView) findViewById(R.id.last_tv);
        mTakeTV = (TextView) findViewById(R.id.take_tv);
        mSkipTextView = (TextView) findViewById(R.id.skip_tv);
        mFiltTextView = (TextView) findViewById(R.id.filt_tv);
        mDoubanceTextView = (TextView) findViewById(R.id.debounce_tv);
    }
    public void distinct(View view){
        /**
         * 此操作符为过滤操作符，过滤掉重复的数据
         */
        mStringBuilder = new StringBuilder();
        Flowable.just(1,2,3,2,4,5,1).distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "distinct:" + integer);
                        mStringBuilder.append( integer+ "\n");
                        mDistinctTV.setText(mStringBuilder.toString());
                    }
                });
    }
    public void elementAt(View view){
        /**
         * 此操作符为发送指定下标的数据
         */
        Flowable.just(1,2,3).elementAt(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "elementAt:" + integer);
                mElementAtTV.setText("" + integer);
            }
        });
    }
    public void first(View view){
        /***
         * 此操作符为发送第一个数据操作符
         */
        Flowable.just(2,1,2,3,4).first(10).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "first:" + integer);
                mFirstTV.setText("" + integer);
            }
        });
    }
    public  void last(View view){
        /**
         * 此操作符为发送最后一个数据操作符
         */
        Flowable.just(1,2,3,4,5).last(1024).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "last:" + integer);
                mLastTV.setText(integer +  "");
            }
        });
    }
    public void take(View v){
        /**
         * 此操作符为取数操作符
         */
        mStringBuilder = new StringBuilder();
        Flowable.just(1,2,3,4,5,6,7,8,9).take(3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "take:" + integer);
                mStringBuilder.append(integer + "\n");
                mTakeTV.setText(mStringBuilder.toString());

            }
        });
    }
    public void ignoreElements(View view){
        /**
         *此操作符只关心结果不关心发送的数据
         * 调用此方法只关心onError()或者ononComplete();
         */
        Flowable.just(1,2,3.4).ignoreElements().subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Log.i(TAG, "ignoreElements excute");
            }
        });
    }
    public void skip(View view){
        /**
         * 此操作符抑制前n项数据仅发送后几项数据
         */
        mStringBuilder = new StringBuilder();
        Flowable<Integer> flowable = Flowable.just(1, 2, 3, 4, 5).skip(2);
        Flowable<Integer> skipLast = Flowable.just(1, 2, 3, 4, 5).skipLast(2);
        flowable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "skip:" + integer);
                mStringBuilder.append(integer + "\n");
                mSkipTextView.setText(mStringBuilder.toString());
            }
        });
        /***
         * 此操作符是抑制后几项数据的发送
         */
        skipLast.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "skipLast:" + integer);
                mStringBuilder.append(integer + "\n");
                mSkipTextView.setText(mStringBuilder.toString());
            }
        });
    }
    public void filt(View view){
        /**
         * 此操作符只有满足条件的才能发送不然不发送
         */
        mStringBuilder = new StringBuilder();
        Flowable.just(1,2,3,4,5,6).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return (integer>2);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "filt:" + integer);
                mStringBuilder.append(integer + "\n");
                mFiltTextView.setText(mStringBuilder.toString());
            }
        });
    }
    public void debounce(View view){
        /**
         * 此操作符会为我们过滤发送过快的数据
         * 这里有个典型的就是搜索框自动弹出匹配项的使用
         */
        mStringBuilder = new StringBuilder();
        Flowable.just(1,2,3).debounce(2, TimeUnit.SECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "debounce:" + integer);
                mStringBuilder.append(integer + "\n");
                mDoubanceTextView.setText(mStringBuilder.toString());
            }
        });
    }

}
