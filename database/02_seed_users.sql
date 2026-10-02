USE gestion_clinique;

INSERT INTO users (username, password_hash, role)
VALUES
    ('infirmier1', '$2a$10$lDaQDfVgXdX6rgEFvsyDyOKzwzU0aTdkDMdbI8MGvbYsttyIHva.6', 'INFIRMIER'),
    ('generaliste1', '$2a$10$cc.3KTHg.WmZWLL2kYGfwOo82TGixnzTJxbkrJ0W5hnEItldk.fnK', 'GENERALISTE');
