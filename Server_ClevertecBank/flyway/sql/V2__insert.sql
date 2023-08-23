INSERT INTO bank (name, location)
VALUES
    ('Clevertec-Bank', '123 Main St, New York, NY'),
    ('Clevertec-Bank1', '456 Oak Ave, San Francisco, CA'),
    ('Clevertec-Bank2', '789 Elm Rd, Los Angeles, CA'),
    ('Clevertec-Bank2', '101 Pine St, Chicago, IL'),
    ('Clevertec-Bank3', '321 Maple Ave, Boston, MA');

INSERT INTO client (first_name, last_name, email, username, password, bank_id)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'johndoe', 'password123', 1),
    ('Jane', 'Smith', 'jane.smith@example.com', 'janesmith', 'pass456', 2),
    ('Michael', 'Johnson', 'michael.j@example.com', 'michaelj', 'secure789', 1),
    ('Emily', 'Davis', 'emily.d@example.com', 'emilyd', 'safe101', 3),
    ('William', 'Brown', 'william.b@example.com', 'williamb', '1234pass', 2);

INSERT INTO account (account_number, balance, currency, client_id)
VALUES
    ('1234567890', 5000.00, 'USD', 1),
    ('9876543210', 2500.00, 'USD', 2),
    ('4567890123', 7500.00, 'EUR', 3),
    ('7890123456', 3000.00, 'USD', 4),
    ('5678901234', 6000.00, 'EUR', 5),
    ('3456789012', 4500.00, 'USD', 1),
    ('2345678901', 4200.00, 'EUR', 2),
    ('8765432109', 5200.00, 'USD', 3),
    ('6543210987', 6800.00, 'USD', 4),
    ('4321098765', 3200.00, 'EUR', 5);