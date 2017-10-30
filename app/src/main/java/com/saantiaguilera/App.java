package com.saantiaguilera;

import javax.annotation.Nonnull;
import java.util.Arrays;

public final class App {

    private App() throws IllegalAccessException {
        throw new IllegalAccessException("Cant instantiate this class");
    }

    @Nonnull
    private static KnowledgeBase base = new KnowledgeBase();

	public static void main(String[] args) {
        Arrays.stream(args)
                .parallel()
                .forEach(base::answer);
    }

}
