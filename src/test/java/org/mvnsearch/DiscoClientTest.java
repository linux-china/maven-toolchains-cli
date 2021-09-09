package org.mvnsearch;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.pkg.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;


public class DiscoClientTest {
    private DiscoClient discoClient = new DiscoClient();

    @Test
    public void testFindJDK() {
        //version: 6-16
        final Queue<MajorVersion> allMajorVersions = discoClient.getAllMajorVersions();
        //distribution: ORACLE_OPEN_JDK, ORACLE, GRAALVM_CE11, ZULU
        final List<Distribution> distributions = discoClient.getDistributions();
        //distribution for version
        final List<Distribution> distributionsForVersion = discoClient.getDistributionsForVersion(new VersionNumber(11));
        //openjdk-11.0.2_osx-x64_bin.tar.gz
        final List<Pkg> pkgs = discoClient.getPkgs(
                Distribution.ORACLE_OPEN_JDK,
                new VersionNumber(11),
                Latest.OVERALL,
                OperatingSystem.MACOS,
                LibCType.LIBC,
                Architecture.AMD64,
                Bitness.BIT_64,
                ArchiveType.TAR_GZ,
                PackageType.JDK,
                false,
                true,
                ReleaseStatus.GA,  //GA or EA
                TermOfSupport.NONE,  // LTS, MTS, STS
                Scope.DIRECTLY_DOWNLOADABLE);
        pkgs.forEach(pkg -> {
            System.out.println(pkg);
            System.out.println(pkg.getId());
        });
        //pkg for openjdk-11.0.2_osx-x64_bin.tar.gz
        final Pkg pkg = discoClient.getPkg("04fe3c5dcd0c4a21fb8f05ceb33734f8");
        //pkg download url
        final String pkgDirectDownloadUri = discoClient.getPkgDirectDownloadUri(pkg.getEphemeralId(), new SemVer(pkg.getDistributionVersion()));
        System.out.println(pkgDirectDownloadUri);

    }

    @Test
    public void testGraalVMDownload() {
        //graalvm-ce-java11-darwin-amd64-21.2.0.tar.gz
        final List<Pkg> pkgs = discoClient.getPkgs(
                Distribution.GRAALVM_CE11,
                new VersionNumber(21), //GraalVM version starts with 21
                Latest.OVERALL,
                OperatingSystem.MACOS,
                LibCType.LIBC,
                Architecture.AMD64,
                Bitness.BIT_64,
                ArchiveType.TAR_GZ,
                PackageType.JDK,
                false,
                true,
                ReleaseStatus.GA,  //GA or EA
                TermOfSupport.NONE,  // LTS, MTS, STS
                Scope.DIRECTLY_DOWNLOADABLE);
        pkgs.forEach(pkg -> {
            System.out.println(pkg);
            System.out.println(pkg.getId());
        });
    }
}
