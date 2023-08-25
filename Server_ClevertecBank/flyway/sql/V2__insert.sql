INSERT INTO bank (name)
VALUES
    ('Clevertec-Bank'),
    ('Clevertec-Bank1'),
    ('Clevertec-Bank2'),
    ('Clevertec-Bank2'),
    ('Clevertec-Bank3');

INSERT INTO client (first_name, last_name, email, username, password)
VALUES ('John', 'Doe', 'johndoe@email.com', 'johndoe', 'password123'),
       ('Jane', 'Smith', 'janesmith@email.com', 'janesmith', 'mypassword'),
       ('Alice', 'Johnson', 'alicej@email.com', 'alicej', 'alicepassword'),
       ('Bob', 'Williams', 'bobw@email.com', 'bobwill', 'bobbypassword'),
       ('Charlie', 'Brown', 'charlieb@email.com', 'charliebrown', 'charliespassword');

INSERT INTO account (account_number, balance, currency, client_id, bank_id)
VALUES ('1234567890', 15000.00, 'USD', 1, 1),
       ('0987654321', 5000.00, 'EUR', 2, 1),
       ('1122334455', 7000.00, 'GBP', 3, 2),
       ('5566778825', 9500.00, 'JPY', 4, 2),
       ('2233445566', 20000.00, 'CAD', 5, 3),
       ('3344556677', 32000.00, 'AUD', 1, 3),
       ('4455667788', 14000.00, 'USD', 2, 4),
       ('5566778899', 3600.00, 'EUR', 3, 4),
       ('6677889900', 8000.00, 'GBP', 4, 5),
       ('7788990011', 4500.00, 'JPY', 5, 5);