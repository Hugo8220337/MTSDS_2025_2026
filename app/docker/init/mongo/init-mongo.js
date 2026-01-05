db = db.getSiblingDB('admin');
db.createUser({
    user: 'myuser',
    pwd: 'secret',
    roles: [
        { role: 'root', db: 'admin' },
        { role: 'readWrite', db: 'development-db' }
    ]
});

db = db.getSiblingDB('development-db');
db.createCollection('development');

db = db.getSiblingDB('development-db');
db.createCollection('plan_contents');

db = db.getSiblingDB('development-db');
db.createCollection('notifications');