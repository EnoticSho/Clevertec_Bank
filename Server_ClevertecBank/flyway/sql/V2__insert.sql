INSERT INTO bank (name)
VALUES
    ('Clevertec-Bank'),
    ('Clevertec-Bank1'),
    ('Clevertec-Bank2'),
    ('Clevertec-Bank2'),
    ('Clevertec-Bank3');

INSERT INTO client (first_name, last_name, username, password)
VALUES ('Alisa', 'Doe', 'Alisa', '$2a$12$OX6l312SSuvcOAILp0Laj.TCjQnfdnX/u8bmzGBJW2/nhvx7UMhn.'),
       ('Sanya', 'Smith', 'Sanya', 'Sanya'),
       ('Pavel', 'Johnson', 'Pavel', 'Pavel'),
       ('Lesha', 'Williams', 'Lesha', 'Lesha'),
       ('Sergey', 'Brown', 'Sergey', 'Sergey');

INSERT INTO account (account_number, balance, currency, client_id, bank_id)
VALUES ('1234567890', 15000.00, 'BYN', 1, 1),
       ('0987654321', 5000.00, 'BYN', 2, 1),
       ('1122334455', 7000.00, 'BYN', 3, 2),
       ('5566778825', 9500.00, 'BYN', 4, 2),
       ('2233445566', 20000.00, 'BYN', 5, 3),
       ('3344556677', 32000.00, 'USD', 1, 3),
       ('4455667788', 14000.00, 'USD', 2, 4),
       ('5566778899', 3600.00, 'USD', 3, 4),
       ('6677889900', 8000.00, 'USD', 4, 5),
       ('7788990011', 4500.00, 'USD', 5, 5);