package com.saantiaguilera.model.contracts;

import javax.annotation.Nullable;

/**
 * Created by saguilera on 10/29/17.
 */
public interface Matcher<Matchable> {

    boolean matches(@Nullable Matchable matchable);

}
