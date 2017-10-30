package com.saantiaguilera.model.contracts;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Created by saguilera on 10/29/17.
 */
@Immutable
public interface Mutable<Element, ParamsType> {

    @Nonnull
    Element mutate(ParamsType... paramsType);

}
