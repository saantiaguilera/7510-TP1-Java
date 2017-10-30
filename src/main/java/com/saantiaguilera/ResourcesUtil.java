package com.saantiaguilera;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;

/**
 * Created by saguilera on 10/30/17.
 */
public final class ResourcesUtil {

    private ResourcesUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Shouldnt instantiate this");
    }

    @Nonnull
    public static Path getResource(@Nonnull String path) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemClassLoader()
                .getResource(path).toURI());
    }

}
