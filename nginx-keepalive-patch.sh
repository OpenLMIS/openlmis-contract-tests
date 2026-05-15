#!/bin/sh
# Workaround for client→nginx stale-keepalive race seen in contract tests:
# Apache HttpClient pools connections; when nginx FINs an idle one within
# keepalive_timeout (75s default), HttpClient picks it from the pool, writes
# a request into a half-closed socket, and hangs forever waiting for a
# response. Forcing keepalive_timeout=0 makes nginx open a fresh TCP per
# client request — no pool to go stale.
#
# The upstream openlmis/nginx image renders default.conf from a
# consul-template template we don't control. This polls the rendered file
# and re-applies the directive after every render.
set -e
CONFIG=/etc/nginx/conf.d/default.conf
MARKER='keepalive_timeout 0'

while true; do
  if [ -f "$CONFIG" ] \
     && grep -q 'listen 80' "$CONFIG" \
     && ! grep -q "$MARKER" "$CONFIG"; then
    sed -i 's|listen 80;|listen 80;\n  keepalive_timeout 0;|' "$CONFIG"
    nginx -s reload 2>/dev/null || true
    echo "[keepalive-patch] patched at $(date -u +%FT%TZ)" >&2
  fi
  sleep 2
done
