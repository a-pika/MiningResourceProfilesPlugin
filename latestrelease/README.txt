

Configuration:
	Windows 7
	Java 7 64-bit
	ProM 6.5.1
	MySQL Server 5.7


To install the package:

1. Copy folder 'miningresourceprofiles-6.5.1' to ProM packages folder.
2. Add to packages.xml under <installed-packages> entry:

<package name="miningresourceprofiles" version="6.5.1" os="all" url="http://www.yawlfaundation.org" desc="miningresourceprofiles" org="QUT" auto="false" hasPlugins="true" license="LGPL" author="A. Pika" maintainer="A. Pika" logo="">
 </package>

3. Create system variable R_HOME with the value:
[Prom packages path]\miningresourceprofiles-6.5.1\MiningResourceProfiles_lib\R-3.0.0

4. Add to system Path variable the value: 
[Prom packages path]\miningresourceprofiles-6.5.1\MiningResourceProfiles_lib\jrilib

5. Restart Windows