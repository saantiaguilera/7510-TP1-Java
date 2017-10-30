package com.saantiaguilera.model;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.saantiaguilera.model.contracts.Bindable;
import com.saantiaguilera.model.contracts.Matcher;
import com.saantiaguilera.model.contracts.Statement;

/**
 * Created by saguilera on 10/29/17.
 */
public class Fact implements Statement<String>, Bindable<String>, Matcher<Statement<String>> {

    @Nullable
    protected String name;
    @Nullable
    protected List<String> params;

    @Override
    public void bind(@Nonnull String line) throws BoundException {
        if (name != null || params != null) {
            throw new BoundException("Already bound to " + name + ", " + params);
        }

        name = line.replaceAll("\\(.+","").trim();
        params = Arrays.asList(line
                .replaceAll(".*\\(", "")
                .replaceAll("\\).*", "")
                .replaceAll("\\s", "")
                .split(","));
    }

    @Nonnull
    @Override
    public String name() {
        if (name == null) {
            throw new IllegalStateException("Name not found. Forgot to bind?");
        }

        return name;
    }

    @Nonnull
    @Override
    public List<String> params() {
        if (params == null) {
            throw new IllegalStateException("Params not found. Forgot to bind?");
        }

        return params;
    }

    @Override
    public boolean matches(@Nullable Statement<String> statement) {
        return name().contentEquals(statement.name()) &&
               params().parallelStream()
                   .allMatch(param -> statement.params().contains(param));
    }

    @Override
    public boolean equals(Object o) {
        if(!super.equals(o)) {
            return false;
        }

        if (!(o instanceof Fact)) {
            return false;
        }

        Fact fact = (Fact) o;

        return fact.matches(this);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

}
