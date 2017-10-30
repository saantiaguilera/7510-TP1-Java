package com.saantiaguilera.model;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.saantiaguilera.model.contracts.Mutable;

/**
 * Created by saguilera on 10/29/17.
 */
class MutableFact extends Fact implements Mutable<Fact, String> {

    @Nonnull
    @Override
    public Fact mutate(String... paramsType) {
        if (paramsType.length != params().size()) {
            throw new IncompatibleClassChangeError("Param sizes to mutate differ.");
        }

        Fact fact = new Fact();
        fact.name = name();
        fact.params = Arrays.asList(paramsType);
        return fact;
    }

}
