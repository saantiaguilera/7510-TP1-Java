package com.saantiaguilera.model;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.saantiaguilera.ResourcesUtil;
import com.saantiaguilera.model.contracts.Bindable;
import com.saantiaguilera.model.contracts.Matcher;
import com.saantiaguilera.model.contracts.Statement;

/**
 * Created by saguilera on 10/29/17.
 */
public class Rule implements Statement<Fact>, Bindable<String>, Matcher<Statement<String>> {

    @Nullable
    protected MutableFact fact;
    @Nullable
    protected List<MutableFact> params;

    @Override
    public void bind(@Nonnull String line) throws BoundException {
        if (fact != null || params != null) {
            throw new BoundException("Already bound.");
        }
        fact = new MutableFact();
        fact.bind(
            line.replaceAll(":=.*", "")
                .trim()
        );

        params = Arrays.asList(line.replaceAll(".*:=", "")
            .replaceAll("\\s", "")
            .replaceAll("\\),", ");")
            .split(";"))
            .parallelStream()
            .map(factLine -> {
                MutableFact fact = new MutableFact();
                fact.bind(factLine);
                return fact;
            })
            .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public Fact name() {
        if (fact == null) {
            throw new IllegalStateException("Name not found. Forgot to bind?");
        }

        return fact;
    }

    @Nonnull
    @Override
    public List<Fact> params() {
        if (params == null) {
            throw new IllegalStateException("Params not found. Forgot to bind?");
        }

        // Welcome to java generics part 2
        return params.parallelStream()
                .map(mutableFact -> (Fact) mutableFact)
                .collect(Collectors.toList());
    }

    @Nonnull
    @CheckReturnValue
    protected HashMap<String, String> zipmap(@Nonnull Statement<String> statement) {
        Iterator<String> i1 = fact.params().iterator();
        Iterator<String> i2 = statement.params().iterator();

        HashMap<String, String> zipmapVariables = new HashMap<>();

        while (i1.hasNext() && i2.hasNext()) {
            zipmapVariables.put(i1.next(), i2.next());
        }

        if (i1.hasNext() || i2.hasNext()) {
            throw new IllegalStateException("Rule has same name, but defined params dont have same size as query");
        }

        return zipmapVariables;
    }

    @Override
    public boolean matches(@Nullable Statement<String> statement) {
        // If the name is different, dont even waste computation time
        if (!statement.name().contentEquals(fact.name())) {
            return false;
        }

        // Compute a zipmap between our fact (that has mock names) and the statement (real names)
        HashMap<String, String> zipmap = zipmap(statement);

        // Swap the 'mocked' facts of params for real ones with the real names
        List<Fact> realFactParams = params.parallelStream()
                .map(param -> {
                    List<String> realParams =
                            param.params()
                                .parallelStream()
                                .map(zipmap::get)
                                .collect(Collectors.toList());
                    return param.mutate((String[]) realParams.toArray());
                })
                .collect(Collectors.toList());

        // Check that our facts exist in the db
        try {
            return Files
                .lines(ResourcesUtil.getResource("rules.db"))
                .parallel()
                .filter(line -> !line.contains(":="))
                .map(line -> {
                    Fact fact = new Fact();
                    fact.bind(line);
                    return fact;
                })
                .anyMatch(fact -> {
                    if (realFactParams.contains(fact)) {
                        realFactParams.remove(fact);
                    }

                    return realFactParams.isEmpty();
                });
        } catch (Exception silent) {
            return false;
        }
    }

}
