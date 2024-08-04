SUMMARY = "AEGIS build recipe"
DESCRIPTION = "This recipe will build the AEGIS controller program and install a sysvinit script to start it at boot time."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "pigpio pigpio-dev fmt spdlog"

# Load aegis from github repo
SRC_URI = "gitsm://github.com/labros-zotos/aegis.git;protocol=https;branch=main \
           file://init \
          "

# use the latest version in the branch
SRCREV = "${AUTOREV}"

# work directory for source files
S = "${WORKDIR}/git/src"

# inherit autotools pkgconfig

TARGET_CC_ARCH += "${LDFLAGS}"
TARGET_CXX_ARCH += "${LDFLAGS}"

SHTLIB = "lib/raspberry-pi-i2c-sht4x"
SPDLOG_FLAGS = "-DSPDLOG_BUILD_SHARED=on -DSPDLOG_BUILD_EXAMPLE=off -DSPDLOG_FMT_EXTERNAL=on"
do_compile() {
    ${CC} ${CFLAGS} -fPIC -I${SHTLIB} -c ${SHTLIB}/sensirion_common.c -o sensirion_common.o
    ${CC} ${CFLAGS} -fPIC -I${SHTLIB} -c ${SHTLIB}/sensirion_i2c.c -o sensirion_i2c.o
    ${CC} ${CFLAGS} -fPIC -I${SHTLIB} -c ${SHTLIB}/sht4x_i2c.c -o sht4x_i2c.o
    ${CC} ${CFLAGS} -fPIC -I${SHTLIB} -c ${SHTLIB}/sensirion_i2c_hal.c -o sensirion_i2c_hal.o

    ${CXX} ${CXXFLAGS} -Os -Wall -fstrict-aliasing -Wstrict-aliasing=1 -fPIC -I${SHTLIB} -I. \
        -I${STAGING_INCDIR} ${SPDLOG_FLAGS} \
        -c aegis.cpp -o aegis.o 
    ${CXX} ${CXXFLAGS} -Os -Wall -fstrict-aliasing -Wstrict-aliasing=1 -fPIC -I${SHTLIB} -I. \
        -I${STAGING_INCDIR} ${SPDLOG_FLAGS} \
        -c SHT40Sensor.cpp -o SHT40Sensor.o
    ${CXX} ${CXXFLAGS} -Os -Wall -fstrict-aliasing -Wstrict-aliasing=1 -fPIC -I${SHTLIB} -I. \
        -I${STAGING_INCDIR} ${SPDLOG_FLAGS} \
        -c Pump.cpp -o Pump.o
    ${CXX} ${CXXFLAGS} -Os -Wall -fstrict-aliasing -Wstrict-aliasing=1 -fPIC -I${SHTLIB} -I. \
        -I${STAGING_INCDIR} ${SPDLOG_FLAGS} \
        -c GPIOController.cpp -o GPIOController.o
    ${CXX} ${CXXFLAGS} -Os -Wall -fstrict-aliasing -Wstrict-aliasing=1 -fPIC -I${SHTLIB} -I. \
        -I${STAGING_INCDIR} \
        -c Measurement.cpp -o Measurement.o
    ${CXX} ${CXXFLAGS} -Os -Wall -fstrict-aliasing -Wstrict-aliasing=1 -fPIC -I${SHTLIB} -I. \
        -I${STAGING_INCDIR} \
        -c MeasurementLogger.cpp -o MeasurementLogger.o

    ${CXX} ${CXXFLAGS} -o aegis aegis.o SHT40Sensor.o Pump.o GPIOController.o Measurement.o MeasurementLogger.o \
        sensirion_common.o sensirion_i2c.o sht4x_i2c.o sensirion_i2c_hal.o \
        -lpigpio -lrt -pthread -lfmt -lspdlog
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/aegis ${D}${sbindir}
}

inherit update-rc.d

INITSCRIPT_NAME = "aegis"
INITSCRIPT_PARAMS = "defaults 98 02"

do_install:append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/init/aegis-init ${D}${sysconfdir}/init.d/aegis
}

FILES_${PN} += "${sysconfdir}/init.d/aegis"