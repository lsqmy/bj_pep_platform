package com.aratek.framework.domain.base;

public class AraPair<A extends Object, B extends Object> {
    private final A first;
    private final B second;

    public AraPair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "AraPair[" + first + "," + second + "]";
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof AraPair<?, ?> && equals(first, ((AraPair<?, ?>) other).first)
                && equals(second, ((AraPair<?, ?>) other).second);
    }

    @Override
    public int hashCode() {
        if (first == null) {
            return (second == null) ? 0 : second.hashCode() + 1;
        } else if (second == null) {
            return first.hashCode() + 2;
        } else {
            return first.hashCode() * 17 + second.hashCode();
        }
    }

    public static <A, B> AraPair<A, B> of(A a, B b) {
        return new AraPair<A, B>(a, b);
    }

}