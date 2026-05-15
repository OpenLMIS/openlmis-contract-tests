#!/bin/sh
# Workaround for two stale-keepalive races seen in contract tests:
#
# 1. nginx→upstream: each upstream block has `keepalive 128;` so nginx
#    pools backend connections. Under load a pooled connection ends up
#    in CLOSE_WAIT with the response buffered in nginx but never
#    delivered to the client — request hangs forever.
# 2. client→nginx: Apache HttpClient pools connections; when nginx FINs
#    an idle one at keepalive_timeout (75s default), HttpClient picks it
#    from the pool and writes a request into a half-closed socket.
#
# Fixes both by stripping `keepalive 128;` from upstream blocks AND
# setting `keepalive_timeout 0;` in the server block. Every TCP — both
# client→nginx and nginx→backend — becomes a fresh connection.
#
# The upstream openlmis/nginx image renders default.conf from a
# consul-template template we don't control. This polls the rendered
# file and re-applies the patch after every render.
set -e
CONFIG=/etc/nginx/conf.d/default.conf
MARKER='keepalive_timeout 0'

while true; do
  if [ -f "$CONFIG" ] \
     && grep -q 'listen 80' "$CONFIG" \
     && ! grep -q "$MARKER" "$CONFIG"; then
    sed -i 's|listen 80;|listen 80;\n  keepalive_timeout 0;|' "$CONFIG"
    sed -i '/^[[:space:]]*keepalive [0-9]\+;[[:space:]]*$/d' "$CONFIG"
    nginx -s reload 2>/dev/null || true
    echo "[keepalive-patch] patched at $(date -u +%FT%TZ)" >&2
  fi
  sleep 2
done
