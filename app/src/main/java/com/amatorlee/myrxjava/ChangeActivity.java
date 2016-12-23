package com.amatorlee.myrxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by AmatorLee on 2016/12/22.
 * 变换操作符包括但不限于
 * buffer缓存一段数据后作为一各被观察者发送出去
 * map 一次变换
 * flatMap 二次变换
 * groupBy 分类操作符
 * scan 使用函数操作符
 */

public class ChangeActivity extends AppCompatActivity{
    private static final String TAG = "ChangeActivity";
    private TextView mBufferTectView;
    private TextView mMapTextView;
    private TextView mFlatMapTV;
    private TextView mGroupByTV;
    private TextView mScanTextView;
    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        initTextView();

    }

    private void initTextView() {
        mBufferTectView = (TextView) findViewById(R.id.buffer_tv);
        mMapTextView = (TextView) findViewById(R.id.map_tv);
        mFlatMapTV = (TextView) findViewById(R.id.flatMap_tv);
        mGroupByTV = (TextView) findViewById(R.id.groupBy_tv);
        mScanTextView = (TextView) findViewById(R.id.scan_tv);
    }

    public void buffer(View view){
        /**
         * 发送一个缓存集合被观察者，有几个重构方法这里使用的
         * buffer(n)
         * 通过n指定发送集合的个数
         */
        mStringBuilder = new StringBuilder();
        Flowable.just("Hello","AmatorLee","正在使用Buffer","操作符")
                .buffer(3).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                Log.i(TAG, "Buffer:" + strings.toString());
                mStringBuilder.append(strings.toString());
                mBufferTectView.setText(mStringBuilder.toString());
            }
        });

    }
    public void map(View view){
        /**
         * map操作符应该是最简单的一个变换操作符了
         * 作符对原始Observable发射的每一项数据应用一个你选择的函数
         * 然后返回一个发射这些结果的被观察者。
         * 它主要是进行一次变换
         * 这里的例子是给他添加一个尾部
         */
        Flowable.just("AmatorLee")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + "\n" +
                                "正在使用map操作符";
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mMapTextView.setText(s);
                Log.i(TAG, "map:" + s);
            }
        });
    }
    public void flatMap(View view){
        /**
         * 此操作符是进行二次转换的操作符
         * 通常网上的人都说比较难理解，但是还好
         * 网上使用的例子一般有：找出某学生的课程
         * 由于课程不是唯一的所以如果使用map则必须需要遍历课程集合
         * 但是用flat Map则免去中间遍历的过程直接获取各课程名称。
         * 这里使用的操作符是一个简单例子，将整形数值转变为string类型再转变为整形
         * 本例子并无用处仅说明其性质
         */
        mStringBuilder = new StringBuilder();
        Flowable.just(1,2,3).flatMap(new Function<Integer, Publisher<String>>() {
            @Override
            public Publisher<String> apply(Integer integer) throws Exception {
                return Flowable.just(integer.toString());
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "flatMap:" + s);
                mStringBuilder.append(s + "\n");
                mFlatMapTV.setText(mStringBuilder.toString());
            }
        });
    }
    public void groupBy(View view){
        /**
         * groupBy操作符主要是对一个被观察者的序列进行分类产生一个GroupFlowable
         * 通过GroupFlowable可以获取分类的key值
         */
        mStringBuilder = new StringBuilder();
        Flowable.range(1,6).groupBy(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer%2;
            }
        }).subscribe(new Consumer<GroupedFlowable<Integer, Integer>>() {
            @Override
            public void accept(final GroupedFlowable<Integer, Integer> flowable) throws Exception {
                flowable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "groupByKey:" + flowable.getKey() + "groupByValue" + integer);
                        mStringBuilder.append(flowable.getKey() + ":" + integer + "\n");
                        mGroupByTV.setText(mStringBuilder.toString());
                    }
                });
            }
        });
    }
    public void scan(View view){
        /**
         * scan对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为
         *自己的第一项数据发射。它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数
         */
        mStringBuilder = new StringBuilder();
        Flowable.just(1,2,3,4,5).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                Log.i(TAG, "scan1:" + integer + ":" + integer2);
                return integer +  integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "scan:" + integer);
                mStringBuilder.append(integer + "\n");
                mScanTextView.setText(mStringBuilder);
            }
        });
    }

}
