DESCRIPTION = "pigpio"
SECTION = "devel/libs"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://UNLICENCE"

COMPATIBLE_MACHINE = "^rpi$"

SRC_URI = "git://github.com/joan2937/pigpio.git;protocol=https;branch=master;tag=v79"

S = "${WORKDIR}/git"

inherit setuptools3 pkgconfig cmake systemd

SYSTEMD_PACKAGES="${PN}"

DEPENDS = "python3"
RDEPENDS:${PN} = "python3"

PROVIDES = "pigpio pigpio-dev"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN}-doc += "\
     /usr/man/man1/pigs.1 \
     /usr/man/man1/pig2vcd.1 \
     /usr/man/man1/pigpiod.1 \
     /usr/man/man3/pigpio.3 \
     /usr/man/man3/pigpiod_if.3 \
     /usr/man/man3/pigpiod_if2.3 \
"

FILES_${PN} = "${bindir}/* \
    ${libdir}/* \
"

FILES_${PN}-dev += "\
     ${includedir} \
     ${includedir}/pigpio.h \
     ${includedir}/pigpio_if2.h \
     ${includedir}/pigpio_if.h \
"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${libdir}
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
     
    install -m 0644 ${S}/pigpio.py ${D}${PYTHON_SITEPACKAGES_DIR}

    cmake_do_install
}