package org.mvnsearch.service;

import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.Toolchain;

import java.util.List;

public interface ToolchainService {

    List<Toolchain> findAllToolchains();

    @Nullable
    Toolchain findToolchain(String version, @Nullable String vendor);
}
