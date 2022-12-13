/*
 * MIT License
 *
 * Copyright (c) 2022-present Alan Yeh <alan@yeh.cn>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package central.android.promise;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Android Promise
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
public class Promise<R> {
    /// 用于保证 Promise 顺序执行
    private static final ExecutorService barrier = Executors.newSingleThreadExecutor();
    /// 用于在异步处理东西
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(3);
    /// 用于返回主线程
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public enum State {
        /**
         * 等待状态
         */
        Pending,
        /**
         * 成功状态
         */
        Fulfilled,
        /**
         * 失败状态
         */
        Rejected
    }

    private State state;

    /**
     * Promise 当前状态
     */
    public State getState() {
        return state;
    }

    private R result;

    /**
     * Promise 最终结果
     */
    public R getResult() {
        return result;
    }

    private RuntimeException error;

    /**
     * Promise 执行过程中的错误
     */
    public RuntimeException getError() {
        return error;
    }

    /**
     * 判断最后是否执行成功
     */
    public boolean isSuccess() {
        return error == null;
    }

    /**
     * 未执行的Handler
     */
    private List<PromiseResolver<R>> handlers = new ArrayList<>();

    /**
     * 拼接Promise
     * 如果当前Promise还没有执行,则拼接在当前Promise的执行栈中
     * 如果当前Promise已经执行了,则直接将当前Promise的值传给下一个执行者
     *
     * @param resolver 回调
     */
    public void pipe(PromiseResolver<R> resolver) {
        if (this.state == State.Pending) {
            this.handlers.add(resolver);
        } else {
            resolver.resolve(result, error);
        }
    }


    private interface Piper<A, R> {
        void pipe(A arg, RuntimeException error, PromiseResolver<R> resolver);
    }

    /**
     * 创建一个Promise,并拼接在Promise(self)的执行链中
     *
     * @param self  Promise
     * @param piper 水管
     * @param <R>   返回值类型
     * @return Promise
     */
    private <A, R> Promise<R> __pipe(final Promise<A> self, final Promise.Piper<A, R> piper) {
        return new Promise<R>(new PromiseCallbackWithResolver<A, R>() {
            @Override
            public void call(A arg, final PromiseResolver<R> resolver) {
                self.pipe(new PromiseResolver<A>() {
                    @Override
                    public void resolve(A result, RuntimeException error) {
                        piper.pipe(result, error, resolver);
                    }
                });
            }
        });
    }

    /**
     * 创建一个未执行的Promise
     *
     * @param callback PromiseCallbackWithResolver
     */
    public <A> Promise(final PromiseCallbackWithResolver<A, R> callback) {
        this.state = State.Pending;

        final PromiseResolver<R> finalResolver = new PromiseResolver<R>() {
            @Override
            public void resolve(final R result, final RuntimeException error) {
                final List<PromiseResolver<R>> nextPromises = new ArrayList<>();

                //保证执行链的顺序执行
                final CountDownLatch signal = new CountDownLatch(1);
                Promise.barrier.execute(new Runnable() {
                    @Override
                    public void run() {
                        //race
                        if (Promise.this.getState() == State.Pending) {
                            nextPromises.addAll(Promise.this.handlers);

                            Promise.this.result = result;
                            Promise.this.error = error;
                            Promise.this.state = error != null ? State.Rejected : State.Fulfilled;
                        }
                        signal.countDown();
                    }
                });

                try {
                    signal.await();
                } catch (InterruptedException ignored) {
                }

                for (PromiseResolver<R> next : nextPromises) {
                    next.resolve(result, error);
                }
            }
        };

        final PromiseResolver<R> resolver = new PromiseResolver<R>() {
            @Override
            public void resolve(R result, RuntimeException error) {
                if (result != null && error != null) {
                    throw new IllegalArgumentException("不允许同时返回结果和错误, 这将导至计算无法继续");
                }
                // 保证Promise的结果不变性, 如果当前状态不是Pending, 则抛弃结果
                if (Promise.this.state == State.Pending) {
                    finalResolver.resolve(result, error);
                }
            }
        };

        //创建Promise之后, 直接开始执行任务
        Promise.handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.call(null, resolver);
                } catch (RuntimeException ex) {
                    finalResolver.resolve(null, ex);
                }
            }
        });
    }

    /**
     * 主线程执行
     *
     * @param then next step
     * @param <N>  新的返回值类型
     * @return Promise
     */
    public <N> Promise<N> then(final Function<R, N> then) {
        return __pipe(this, new Piper<R, N>() {
            @Override
            public void pipe(final R arg, RuntimeException error, final PromiseResolver<N> resolver) {
                if (error != null) {
                    resolver.resolve(null, error);
                } else {
                    Promise.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                resolver.resolve(then.apply(arg), null);
                            } catch (RuntimeException ex) {
                                resolver.resolve(null, ex);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 主线程执行
     *
     * @param then next step，并返回 Promise
     * @param <N>  新的返回值类型
     * @return Promise
     */
    public <N> Promise<N> thenPromise(final Function<R, Promise<N>> then) {
        return __pipe(this, new Piper<R, N>() {
            @Override
            public void pipe(final R arg, RuntimeException error, final PromiseResolver<N> resolver) {
                if (error != null) {
                    resolver.resolve(null, error);
                } else {
                    Promise.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                then.apply(arg).pipe(resolver);
                            } catch (RuntimeException ex) {
                                resolver.resolve(null, ex);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 异步执行
     *
     * @param then next step
     * @param <N>  新的返回值类型
     * @return Promise
     */
    public <N> Promise<N> thenAsync(final Function<R, N> then) {
        return __pipe(this, new Piper<R, N>() {
            @Override
            public void pipe(final R arg, RuntimeException error, final PromiseResolver<N> resolver) {
                if (error != null) {
                    resolver.resolve(null, error);
                } else {
                    Promise.threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                resolver.resolve(then.apply(arg), null);
                            } catch (RuntimeException ex) {
                                resolver.resolve(null, ex);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 异步执行
     *
     * @param then next step
     * @param <N>  新的返回值类型
     * @return Promise
     */
    public <N> Promise<N> thenAsyncPromise(final Function<R, Promise<N>> then) {
        return __pipe(this, new Piper<R, N>() {
            @Override
            public void pipe(final R arg, RuntimeException error, final PromiseResolver<N> resolver) {
                if (error != null) {
                    resolver.resolve(null, error);
                } else {
                    Promise.threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                then.apply(arg).pipe(resolver);
                            } catch (RuntimeException ex) {
                                resolver.resolve(null, ex);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 延迟执行
     *
     * @param delayMillis 延迟时间，毫秒
     * @param then        下一步
     * @param <N>         返回值类型
     * @return Promise
     */
    public <N> Promise<N> thenDelay(final long delayMillis, final Function<R, N> then) {
        return __pipe(this, new Piper<R, N>() {
            @Override
            public void pipe(final R arg, RuntimeException error, final PromiseResolver<N> resolver) {
                if (error != null) {
                    resolver.resolve(null, error);
                } else {
                    Promise.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                resolver.resolve(then.apply(arg), null);
                            } catch (RuntimeException ex) {
                                resolver.resolve(null, ex);
                            }
                        }
                    }, delayMillis);
                }
            }
        });
    }

    /**
     * 同步处理错误
     *
     * @param callback error handler
     * @return Promise
     */
    public Promise<R> error(final Function<RuntimeException, R> callback) {
        return __pipe(this, new Piper<R, R>() {
            @Override
            public void pipe(final R arg, final RuntimeException error, final PromiseResolver<R> resolver) {
                if (error != null) {
                    Promise.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                resolver.resolve(callback.apply(error), null);
                            } catch (RuntimeException ex) {
                                resolver.resolve(null, ex);
                            }
                        }
                    });
                } else {
                    resolver.resolve(arg, null);
                }
            }
        });
    }

    /**
     * 异步处理错误
     *
     * @param callback error handler
     * @return Promise
     */
    public Promise<R> errorAsync(final Function<RuntimeException, R> callback) {
        return __pipe(this, new Piper<R, R>() {
            @Override
            public void pipe(final R arg, final RuntimeException error, final PromiseResolver<R> resolver) {
                if (error != null) {
                    Promise.threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                resolver.resolve(callback.apply(error), null);
                            } catch (RuntimeException ex) {
                                resolver.resolve(null, ex);
                            }
                        }
                    });
                } else {
                    resolver.resolve(arg, null);
                }
            }
        });
    }

    /**
     * 主线程执行,正确或失败都会执行
     *
     * @param always handle always
     * @param <N>    返回值类型
     * @return Promise
     */
    public <N> Promise<N> always(final Function<Object, N> always) {
        return __pipe(this, new Piper<R, N>() {
            @Override
            public void pipe(final R arg, final RuntimeException error, final PromiseResolver<N> resolver) {
                Promise.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            resolver.resolve(always.apply(error != null ? error : arg), null);
                        } catch (RuntimeException ex) {
                            resolver.resolve(null, ex);
                        }
                    }
                });
            }
        });
    }

    /**
     * 异步执行,正确或失败都会执行
     *
     * @param always handle always
     * @param <N>    返回值类型
     * @return Promise
     */
    public <N> Promise<N> alwaysAsync(final Function<Object, N> always) {
        return __pipe(this, new Piper<R, N>() {
            @Override
            public void pipe(final R arg, final RuntimeException error, final PromiseResolver<N> resolver) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            resolver.resolve(always.apply(error != null ? error : arg), null);
                        } catch (RuntimeException ex) {
                            resolver.resolve(null, ex);
                        }
                    }
                });
            }
        });
    }


////////////////////////////////////////////////////////////////////////////////////////////////////
// 静态创建方法
////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Promise<Void> empty() {
        return Promise.just(new Supplier<Void>() {
            @Override
            public Void get() {
                return null;
            }
        });
    }

    /**
     * 创建一个正常返回的 Promise
     */
    public static <R> Promise<R> just(R result) {
        return new Promise<>(new PromiseCallbackWithResolver<Void, R>() {
            @Override
            public void call(Void arg, PromiseResolver<R> resolver) {
                resolver.resolve(result, null);
            }
        });
    }

    /**
     * 在当前线程创建 Promise
     */
    public static <R> Promise<R> just(Supplier<R> supplier) {
        try {
            return Promise.just(supplier.get());
        } catch (RuntimeException ex) {
            return Promise.error(ex);
        }
    }

    /**
     * 创建一个错误的 Promise
     */
    public static <R> Promise<R> error(RuntimeException error) {
        return new Promise<>(new PromiseCallbackWithResolver<Void, R>() {
            @Override
            public void call(Void arg, PromiseResolver<R> resolver) {
                resolver.resolve(null, error);
            }
        });
    }

    /**
     * 创建一个错误的 Promise
     */
    public static <R> Promise<R> error(String message) {
        return Promise.error(new RuntimeException(message));
    }

    /**
     * 创建同步 Promise
     */
    public static <R> Promise<R> sync(Supplier<R> supplier) {
        return new Promise<>(new PromiseCallbackWithResolver<Void, R>() {
            @Override
            public void call(Void arg, PromiseResolver<R> resolver) {
                Promise.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            resolver.resolve(supplier.get(), null);
                        } catch (RuntimeException ex) {
                            resolver.resolve(null, ex);
                        }
                    }
                });
            }
        });
    }

    /**
     * 创建异步 Promise
     */
    public static <R> Promise<R> async(Supplier<R> supplier) {
        return new Promise<>(new PromiseCallbackWithResolver<Void, R>() {
            @Override
            public void call(Void arg, PromiseResolver<R> resolver) {
                Promise.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            resolver.resolve(supplier.get(), null);
                        } catch (RuntimeException ex) {
                            resolver.resolve(null, ex);
                        }
                    }
                });
            }
        });
    }

    /**
     * 包装一系列的Promise对象,返回一个包装后的Promise对象,称之为A
     * 1. 当所有的Promise对象都变成成功态(Fulfilled)后,这个包装后的A才会把自己变成成功状态.
     * A会等最慢的那个Promise对象变成成功态(Fulfilled)后才把自己变成成功态.
     * 2. 只要其中一个Promise对象变成失败态(Rejected),包装后的A就会变成Rejected,并且每一个Rejected传递的值,
     * 会传递给A后面的catch
     *
     * @param promises List of promise
     * @param <R>      返回值类型
     * @return Promise
     */

    public static <R> Promise<Collection<R>> all(final List<Promise<R>> promises) {
        return new Promise<>(new PromiseCallbackWithResolver<Void, Collection<R>>() {
            @Override
            public void call(Void arg, final PromiseResolver<Collection<R>> resolver) {
                final AtomicInteger totalCount = new AtomicInteger(promises.size());
                for (Promise<R> promise : promises) {
                    promise.pipe(new PromiseResolver<R>() {
                        @Override
                        public void resolve(R result, RuntimeException error) {
                            if (error != null) {
                                resolver.resolve(null, error);
                            } else if (totalCount.decrementAndGet() == 0) {
                                List<R> results = new ArrayList<>(promises.size());
                                for (Promise<R> promise : promises) {
                                    results.add(promise.result);
                                }
                                resolver.resolve(results, null);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 包装一系列的Promise对象,返回一个包装后的Promise对象,称之为A
     * 1. 当所有的Promise对象都变成成功态(Fulfilled)后,这个包装后的A才会把自己变成成功状态.
     * A会等最慢的那个Promise对象变成成功态(Fulfilled)后才把自己变成成功态.
     * 2. 只要其中一个Promise对象变成失败态(Rejected),包装后的A就会变成Rejected,并且每一个Rejected传递的值,
     * 会传递给A后面的catch
     *
     * @param supplier List of promise
     * @param <R>      返回值类型
     * @return Promise
     */
    public static <R> Promise<Collection<R>> all(Supplier<List<Promise<R>>> supplier) {
        try {
            return Promise.all(supplier.get());
        } catch (RuntimeException ex) {
            return Promise.error(ex);
        }
    }

    /**
     * 包装一列列的Promise对象,返回一个包装后的Promise对象,称之为R
     * 1. 只要其中的一个Promise对象变成成功态(Fulfilled)后,这个包装后的R就会变成成功态(Fulfilled).
     * 2. 当所有的promise对象都变成失败态(Rejected)后,这个包装后的R才会变成失败态.
     *
     * @param promises List of promise
     * @param <R>      返回值类型
     * @return Promise
     */
    public static <R> Promise<R> race(final List<Promise<R>> promises) {
        return new Promise<>(new PromiseCallbackWithResolver<Void, R>() {
            @Override
            public void call(Void arg, final PromiseResolver<R> resolver) {
                final AtomicInteger totalCount = new AtomicInteger(promises.size());
                for (Promise<R> promise : promises) {
                    promise.pipe(new PromiseResolver<R>() {
                        @Override
                        public void resolve(R result, RuntimeException ex) {
                            if (ex == null) {
                                resolver.resolve(result, null);
                            } else if (totalCount.decrementAndGet() == 0) {
//                                List<RuntimeException> errors = new ArrayList<>();
//                                for (Promise<R> promise : promises){
//                                    errors.add(promise.getError());
//                                }
                                resolver.resolve(null, new RuntimeException("all promise were rejected."));
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 包装一列列的Promise对象,返回一个包装后的Promise对象,称之为R
     * 1. 只要其中的一个Promise对象变成成功态(Fulfilled)后,这个包装后的R就会变成成功态(Fulfilled).
     * 2. 当所有的promise对象都变成失败态(Rejected)后,这个包装后的R才会变成失败态.
     *
     * @param supplier List of promise
     * @param <R>      返回值类型
     * @return Promise
     */
    public static <R> Promise<R> race(Supplier<List<Promise<R>>> supplier) {
        try {
            return Promise.race(supplier.get());
        } catch (RuntimeException ex) {
            return Promise.race(supplier.get());
        }
    }

    /**
     * 包装一系列的Promise对象,返回一个包装后的Promise对象,称之为A
     * 1. 当所有的Promise对象都变成成功态(Fulfilled)后,这个包装后的A才会把自己变成成功状态.
     * A会等最慢的那个Promise对象变成成功态(Fulfilled)后才把自己变成成功态.
     * 2. 只要其中一个Promise对象变成失败态(Rejected),包装后的A就会变成Rejected,并且每一个Rejected传递的值,
     * 会传递给A后面的catch
     *
     * @param promises Map of promise
     * @param <R>      返回值类型
     * @return Promise
     */
    public static <R> Promise<Map<String, R>> map(final Map<String, Promise<R>> promises) {
        return new Promise<>(new PromiseCallbackWithResolver<Void, Map<String, R>>() {
            @Override
            public void call(Void arg, final PromiseResolver<Map<String, R>> resolver) {
                final AtomicInteger totalCount = new AtomicInteger(promises.size());
                for (final Map.Entry<String, Promise<R>> item : promises.entrySet()) {
                    item.getValue().pipe(new PromiseResolver<R>() {
                        @Override
                        public void resolve(R result, RuntimeException error) {
                            if (error != null) {
                                resolver.resolve(null, error);
                            } else if (totalCount.decrementAndGet() == 0) {
                                Map<String, R> results = new HashMap<>(promises.size());
                                for (Map.Entry<String, Promise<R>> entry : promises.entrySet()) {
                                    results.put(entry.getKey(), entry.getValue().getResult());
                                }
                                resolver.resolve(results, null);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 包装一系列的Promise对象,返回一个包装后的Promise对象,称之为A
     * 1. 当所有的Promise对象都变成成功态(Fulfilled)后,这个包装后的A才会把自己变成成功状态.
     * A会等最慢的那个Promise对象变成成功态(Fulfilled)后才把自己变成成功态.
     * 2. 只要其中一个Promise对象变成失败态(Rejected),包装后的A就会变成Rejected,并且每一个Rejected传递的值,
     * 会传递给A后面的catch
     *
     * @param supplier Map of promise
     * @param <R>      返回值类型
     * @return Promise
     */
    public static <R> Promise<Map<String, R>> map(Supplier<Map<String, Promise<R>>> supplier) {
        try {
            return Promise.map(supplier.get());
        } catch (RuntimeException ex) {
            return Promise.error(ex);
        }
    }
}
