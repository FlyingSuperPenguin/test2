package com.example.javalib;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyClass {
    public static void main(String args[]){
        System.out.println("6666");
        test();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("before sleep");
                    Thread.sleep(2000);
                    System.out.println("after sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void test(){
        System.out.println("test currentThread="+Thread.currentThread());
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("subscribe  currentThread="+Thread.currentThread()+"  io="+ Schedulers.io());
                System.out.println("subscribe 1");
                emitter.onNext("haha");
                System.out.println("subscribe 2");
                emitter.onNext("aaaa");
                System.out.println("subscribe 3");
                emitter.onNext("wwww");
                System.out.println("subscribe 4");
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.single())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                Log.d("TAG","onSubscribe  d="+d);
                        System.out.println("onSubscribe currentThread="+Thread.currentThread());
                    }

                    @Override
                    public void onNext(String s) {
//                Log.d("TAG","onNext  s="+s);
                        System.out.println("onNext  s="+s +"currentThread="+Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {
//                Log.d("TAG","onError  e="+e);
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
//                Log.d("TAG","onComplete");
                        System.out.println("onComplete");
                    }
                });
    }

    public static void test2(){
        System.out.println("test2 1");
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("Observable thread is : " + Thread.currentThread().getName());
                System.out.println("emit 1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("Observer thread is :" + Thread.currentThread().getName());
                System.out.println("onNext: " + integer);
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(consumer);

    }
}
