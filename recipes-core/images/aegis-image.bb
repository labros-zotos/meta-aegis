SUMMARY = "Recipe to build an image containing the AEGIS controller program"

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image
inherit extrausers

IMAGE_ROOTFS_SIZE ?= "204800"
IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"

# EXTRA_USERS_PARAMS = "usermod -p 'toor' root"

# Include aegis and its dependencies
IMAGE_INSTALL += "pigpio pigpio-dev fmt spdlog aegis"