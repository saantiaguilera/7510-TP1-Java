package com.saantiaguilera.model.contracts;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by saguilera on 10/29/17.
 */
public interface Statement<Element> {

    @Nonnull
    Element name();

    @Nonnull
    List<Element> params();

}
