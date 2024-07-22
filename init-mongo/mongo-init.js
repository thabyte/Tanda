db = db.getSiblingDB('daraja');
db.createUser({
    user: "admin",
    pwd: "password",
    roles: [{ role: "readWrite", db: "daraja" }]
});