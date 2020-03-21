#! /bin/bash
mysql -uroot -pMyNewPass <nwdafSchema.sql &&
addr="/opt/nwdaf/bin"
if [ $# -eq 0 ]
then
dir="/opt/nwdaf"
if [ -d "$dir" ]
then
rm -rf $dir
fi
mkdir -p $addr &&
cp start.sh $addr &&
cp stop.sh $addr &&
cp nwdaf.jar $addr
else
custom_addr=$1
custom_addr+="$addr"
dir="$1/opt"
if [ -d "$dir" ]
then
rm -rf $dir
fi
mkdir -p $custom_addr &&
cp start.sh $custom_addr &&
cp stop.sh $custom_addr &&
cp nwdaf.jar $custom_addr
fi
