INSERT INTO bank (name, location)
VALUES
    ('Bank of America', '123 Main St, New York, NY'),
    ('JP Morgan Chase', '456 Oak Ave, San Francisco, CA'),
    ('Wells Fargo', '789 Elm Rd, Los Angeles, CA'),
    ('Citibank', '101 Pine St, Chicago, IL'),
    ('TD Bank', '321 Maple Ave, Boston, MA');

INSERT INTO client (first_name, last_name, phone_number)
VALUES
    ('John', 'Doe', '123-456-7890'),
    ('Jane', 'Smith', '987-654-3210'),
    ('Alice', 'Johnson', '555-123-4567'),
    ('Bob', 'Wilson', '333-999-7777'),
    ('Eva', 'Brown', '777-888-1111');

INSERT INTO account (account_number, client_id, bank_id, balance, currency)
VALUES
    ('1234567890', 1, 1, 5000.00, 'USD'),
    ('9876543210', 2, 2, 7000.00, 'USD'),
    ('5555555555', 3, 3, 3000.00, 'EUR'),
    ('1111222233', 4, 1, 1500.00, 'USD'),
    ('4444333322', 5, 2, 9000.00, 'USD'),
    ('8888777766', 1, 1, 2500.00, 'EUR'),
    ('6666888844', 2, 3, 4500.00, 'USD'),
    ('9999888877', 3, 1, 6000.00, 'USD'),
    ('7777999911', 4, 2, 8000.00, 'EUR'),
    ('1234987654', 5, 3, 3500.00, 'USD');