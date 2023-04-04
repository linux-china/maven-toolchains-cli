package org.mvnsearch.aot;

import org.mvnsearch.commands.ListVendors;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.Toolchains;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class ProjectRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(Toolchains.class);
        hints.reflection().registerType(Toolchain.class);
        hints.reflection().registerType(ListVendors.class);
        hints.serialization().registerType(Toolchains.class);
        hints.serialization().registerType(Toolchain.class);
    }
}
