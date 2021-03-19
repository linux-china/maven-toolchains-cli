package org.mvnsearch.service;

import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.Toolchains;

import java.util.List;

public interface ToolchainService {

    Toolchains readToolchains();


    List<Toolchain> findAllToolchains();

    @Nullable
    Toolchain findToolchain(String version, @Nullable String vendor);


    boolean deleteToolChain(String version, @Nullable String vendor);

    boolean addToolChain(String version, @Nullable String vendor, String jdkHome);
}
