package org.mvnsearch.service.impl;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.pkg.*;
import org.mvnsearch.service.FoojayService;

import java.util.List;


public class FoojayServiceImpl extends JdkDistributionSupport implements FoojayService {
    private DiscoClient discoClient = new DiscoClient();

    @Override
    public List<Pkg> findReleases(String version, String vendor) throws Exception {
        VersionNumber majorVersion = new VersionNumber(17);
        if (version.startsWith("1.8")) {
            majorVersion = new VersionNumber(8);
        }
        if (version.contains(".")) {
            majorVersion = new VersionNumber(Integer.valueOf(version.substring(0, version.indexOf("."))));
        }
        final Distribution distribution = Distribution.fromText(vendor);
        OperatingSystem os;
        String osName = System.getProperty("os.name").toLowerCase();
        ArchiveType archiveType = ArchiveType.TAR_GZ;
        if (osName.contains("mac")) {
            os = OperatingSystem.MACOS;
        } else if (osName.contains("windows")) {
            os = OperatingSystem.WINDOWS;
            archiveType = ArchiveType.ZIP;
        } else {
            os = OperatingSystem.LINUX;
        }
        Architecture arch = Architecture.X64;
        Bitness bitness = Bitness.BIT_64;
        String archName = System.getProperty("os.arch").toLowerCase();
        if (archName.contains("x86_32") || archName.contains("amd32")) {
            arch = Architecture.X86;
            bitness = Bitness.BIT_32;
        } else if (archName.contains("aarch64") || archName.contains("arm64")) {
            arch = Architecture.AARCH64;
        }
        return discoClient.getPkgs(
                distribution,
                majorVersion,
                Latest.OVERALL,
                os,
                LibCType.LIBC,
                arch,
                bitness,
                archiveType,
                PackageType.JDK,
                false,
                true,
                ReleaseStatus.GA,  //GA or EA
                TermOfSupport.NONE,  // LTS, MTS, STS
                Scope.DIRECTLY_DOWNLOADABLE);
    }

}
