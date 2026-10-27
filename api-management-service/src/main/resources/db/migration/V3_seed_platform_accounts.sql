Use gatekeeper_management;

INSERT INTO management_users (
    organization_id, role_id, username, password_hash, enabled, created_at, updated_at
)
SELECT NULL, roles.id, 'platform-admin',
       '$2y$10$E/Qaay/8upXkzOQivswrx.i9qy/DkCljpmBCfZZpLIDCo.SZnNybO',
       TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM roles WHERE roles.name = 'PLATFORM_ADMIN';


INSERT INTO management_users (
    organization_id, role_id, username, password_hash, enabled, created_at, updated_at
)
SELECT NULL, roles.id, 'gateway-service',
       '$2y$10$kgkpKYK3zLNzLV54ObZbee68JvjRjy79J6kATSfD0grUXKDd6jxOO',
       TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM roles WHERE roles.name = 'GATEWAY_SERVICE';


INSERT INTO management_users (
    organization_id, role_id, username, password_hash, enabled, created_at, updated_at
)
SELECT NULL, roles.id, 'platform-superadmin', 
       '$2a$10$wE9mHl0nQjKj9jMv2k1P3eT8mXzKz7Z8uI9O8P7Q6R5S4T3U2V1W2', 
       TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM roles WHERE roles.name = 'PLATFORM_ADMIN';

-- password ->AdminPassword123!
