package org.mvnsearch.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "toolchains")
public class Toolchains {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Toolchain> toolchain = new ArrayList<>();

    public List<Toolchain> getToolchain() {
        return toolchain;
    }

    public void setToolchain(List<Toolchain> toolchain) {
        this.toolchain = toolchain;
    }

    public Toolchain findToolchain(String version, @Nullable String vendor) {
        return getToolchain().stream()
                .filter(toolchain -> toolchain.findVersion().equals(version))
                .filter(toolchain -> vendor == null || vendor.equals(toolchain.findVendor()))
                .findFirst().orElse(null);
    }
}
