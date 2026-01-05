#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<EOF
CREATE DATABASE "academins-db";
CREATE DATABASE "applications-db";
CREATE DATABASE "enrollments-db";
CREATE DATABASE "schedules-db";
CREATE DATABASE "notifications-db";
CREATE DATABASE "assements-planning-db";
EOF
