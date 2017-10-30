package com.saantiaguilera.model.contracts;

import javax.annotation.Nonnull;

/**
 * Created by saguilera on 10/29/17.
 */
public interface Bindable<Binding> {

    void bind(@Nonnull Binding binding) throws BoundException;

    class BoundException extends RuntimeException {

        public BoundException(String s) {
            super(s);
        }

    }

}
