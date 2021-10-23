package org.mvnsearch.service.impl;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.pkg.*;
import org.mvnsearch.service.FoojayService;
import org.mvnsearch.service.JdkDownloadLink;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FoojayServiceImpl extends JdkDistributionSupport implements FoojayService {
    private DiscoClient discoClient = new DiscoClient();

    @Override
    public JdkDownloadLink findRelease(String version, String vendor) throws Exception {
        VersionNumber majorVersion;
        if (version.startsWith("1.8")) {
            majorVersion = new VersionNumber(8);
        } else if (version.contains(".")) {
            final String[] parts = version.split("\\.", 3);
            majorVersion = new VersionNumber(Integer.parseInt(parts[0]), Integer.valueOf(parts[1]));
        } else {
            majorVersion = new VersionNumber(Integer.valueOf(version));
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
        final List<Pkg> pkgs = discoClient.getPkgs(
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
        if (pkgs != null && !pkgs.isEmpty()) {

            String pkgId = pkgs.get(0).getId();
            final Pkg pkg = discoClient.getPkg(pkgId);
            final String pkgDirectDownloadUri = discoClient.getPkgDirectDownloadUri(pkg.getEphemeralId(), new SemVer(pkg.getDistributionVersion()));
            return new JdkDownloadLink(pkgDirectDownloadUri, pkg.getFileName());
        }
        return null;
    }

}
