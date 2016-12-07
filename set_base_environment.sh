IP_ADDRESS=
while IFS=$': \t' read -a line ;do
    [ -z "${line%inet}" ] && ip=${line[${#line[1]}>4?1:2]} &&
        [ "${ip#127.0.0.1}" ] && IP_ADDRESS=$ip
  done< <(LANG=C /sbin/ifconfig)

sed -e "s/VIRTUAL_HOST=localhost/VIRTUAL_HOST=$IP_ADDRESS/g" -i .env
sed -e "s/CONSUL_HOST=localhost/CONSUL_HOST=$IP_ADDRESS/g" -i .env
