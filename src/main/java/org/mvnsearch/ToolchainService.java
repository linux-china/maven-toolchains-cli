package org.mvnsearch;

import org.mvnsearch.model.Toolchain;

import java.util.List;

public interface ToolchainService {

    List<Toolchain> findAllToolchains();
}
