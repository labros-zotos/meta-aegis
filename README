# meta-aegis

Yocto layer for AEGIS - <https://github.com/labros-zotos/aegis/>.

This Yocto layer is designed to build an image for AEGIS, the Autonomous Irrigation Controller for Frost Damage Prevention. 

This layer ensures that AEGIS is built and enabled on startup with sysvinit.

## Dependencies

  URI: <https://git.yoctoproject.org/meta-raspberrypi>\
  branch: scarthgap\
  revision: HEAD

  URI: <https://git.yoctoproject.org/poky>\
  branch: scarthgap\
  revision: HEAD

## Adding the meta-aegis layer to your build

1. Add this layer to bblayers.conf and the dependencies above
2. Set MACHINE in local.conf to *raspberrypi4-64*
3. Enable I2C in local.conf by adding the following (i2c-tools *optional*)
    ```
    ENABLE_I2C = "1"
    KERNEL_MODULE_AUTOLOAD:rpi += "i2c-dev i2c-bcm2708"
    IMAGE_INSTALL:append = " i2c-tools"
    ```
4. Enable build environment with: `source poky/oe-init-build-env <your-build-env-dir>`
5. Build with `bitbake aegis-image`
6. Use bmaptool to copy the generated .wic.bz2 file to the SD card
7. Boot your Raspberry Pi
8. AEGIS executable can be found at `/usr/sbin/aegis` and it's init script at `/etc/init.d/aegis`

## Maintainers

Maintainer: Labros Zotos <zotos.lab@gmail.com>
