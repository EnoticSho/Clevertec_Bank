CREATE TABLE bank (
                      bank_id serial PRIMARY KEY,
                      name VARCHAR(255) NOT NULL
);

CREATE TABLE client (
                        client_id serial PRIMARY KEY,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL
);

CREATE TYPE currency_type AS ENUM ('BYN', 'USD', 'EUR', 'GBP', 'JPY', 'CAD', 'AUD', 'Other');

CREATE TYPE transaction_type AS ENUM ('Deposit', 'Withdrawal', 'Transfer', 'Payment', 'Other');

CREATE TABLE account (
                         account_id serial PRIMARY KEY,
                         account_number VARCHAR(20) UNIQUE NOT NULL,
                         balance DECIMAL(10, 2) NOT NULL,
                         currency currency_type NOT NULL DEFAULT 'USD',
                         client_id INT REFERENCES client(client_id) ON DELETE CASCADE,
                         bank_id INT REFERENCES bank(bank_id) ON DELETE CASCADE
);

CREATE TABLE transaction (
                             transaction_id serial PRIMARY KEY,
                             amount DECIMAL(10, 2) NOT NULL,
                             transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             sender_account_id INT,
                             receiver_account_id INT,
                             transaction_type transaction_type NOT NULL,
                             FOREIGN KEY (sender_account_id) REFERENCES account (account_id),
                             FOREIGN KEY (receiver_account_id) REFERENCES account (account_id)
);