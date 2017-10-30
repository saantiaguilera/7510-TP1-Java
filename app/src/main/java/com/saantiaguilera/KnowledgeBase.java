package com.saantiaguilera;

import java.nio.file.Files;

import javax.annotation.Nonnull;

import com.saantiaguilera.model.Fact;
import com.saantiaguilera.model.Rule;
import com.saantiaguilera.model.contracts.Bindable;
import com.saantiaguilera.model.contracts.Matcher;
import com.saantiaguilera.model.contracts.Statement;

public class KnowledgeBase {

	@SuppressWarnings("unchecked")
    public boolean answer(
	        @Nonnull String query) {
		try {
		    final Fact input = new Fact();
		    input.bind(query);

			return Files
                    .lines(ResourcesUtil.getResource("rules.db"))
                    .parallel()
                    .map(line -> {
                        Bindable<String> bindable;
                        if (line.contains(":-")) {
                            bindable = new Rule();
                        } else {
                            bindable = new Fact();
                        }
                        bindable.bind(line);

                        // Welcome to java generics pt1:
                        // This is because java generics are implemented awfully
                        // and since it doesnt support <supers> nor multiple inheritances
                        // generics as paramters in method generics
                        // we are forced to switch interfaces between methods,
                        // else the compiler wont be able
                        // to match classes and will throw IncompatibleTypesExs.
                        return (Matcher<Statement<?>>) bindable;
                    })
                    .anyMatch(type -> type.matches(input));
		} catch (Exception e) {
			return false;
		}
	}

}
