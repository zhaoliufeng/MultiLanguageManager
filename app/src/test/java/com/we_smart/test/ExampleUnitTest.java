package com.we_smart.test;

import android.util.Log;
import android.util.SparseArray;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";

    //基本发送订阅输出
    @Test
    public void just() throws Exception {
        Observable.just(1, 2, 3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("accept : " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        throwable.printStackTrace();
                    }
                });
    }

    @Test
    public void observable() throws Exception {
//        Observable
//                .create(new ObservableOnSubscribe<Integer>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                        for (int i = 0; i < 30; i++) {
//                            System.out.println("发送线程 " + Thread.currentThread().getName() + "---->发送 " + i);
//                            Thread.sleep(1000);
//                            e.onNext(i);
//                        }
//                        e.onComplete();
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
//                .map(new Function<Integer, Integer>() {
//                    @Override
//                    public Integer apply(Integer integer) throws Exception {
//                        return null;
//                    }
//                }).subscribeOn(Schedulers.newThread())
//                .subscribe(
//                        new Consumer<Integer>() {
//                            @Override
//                            public void accept(Integer i) throws Exception {
//                                System.out.println("接收线程 " + Thread.currentThread().getName() + "---->接收 " + i);
//                            }
//                        });
//        Observable.just(1, 2, 3, 4, 5)
//                .filter(new Predicate<Integer>() {
//                    @Override
//                    public boolean test(Integer integer) throws Exception {
//                        return integer % 2 == 0;
//                    }
//                })
//                .subscribe(new Observer<Integer>() {
//                    private Disposable disposable;
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        System.out.println(integer);
//                        if (integer == 2){
//                            disposable.dispose();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

//        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
//            @Override
//            public String apply(String s, Integer integer) throws Exception {
//                return s + integer;
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                System.out.println(s);
//            }
//        });

//        getIntegerObservable()
//                .flatMap(new Function<Integer, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(Integer integer) throws Exception {
//                        List<String> list = new LinkedList<>();
//                        for (int i = 0; i < 3; i++) {
//                            list.add("I am value " + integer);
//                        }
//                        return Observable.fromIterable(list);
//                    }
//                })
//                .doOnNext(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        System.out.println(s);
//                    }
//                }).subscribe();

        ITest iTest = (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), new Class<?>[]{ITest.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //判断方法是不是Object的方法 如果是Object方法执行方法但不输出
                if (method.getDeclaringClass() == Object.class){
                    System.out.println("Is Object method");
                    return method.invoke(this, args);
                }
                Integer a = (Integer) args[0];
                System.out.println("方法名 " + method.getName() + " 参数值 " + a + " 注解 " + method.getAnnotation(GET.class).value());
                return null;
            }
        });

        iTest.add(1);
        iTest.toString();
    }

    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 3; i++) {
                    e.onNext(i);
                }
            }
        });
    }

    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    e.onNext(i + " String ");
                }
            }
        });
    }

    private interface ITest{
        @GET("/version")
        public void add(int a);
    }
}