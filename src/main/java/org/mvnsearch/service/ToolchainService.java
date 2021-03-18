package org.mvnsearch.service;

import org.mvnsearch.model.Toolchain;

import java.util.List;

public interface ToolchainService {

    List<Toolchain> findAllToolchains();
}
