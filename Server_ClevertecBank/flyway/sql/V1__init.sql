CREATE TABLE bank (
                      bank_id serial PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      location VARCHAR(255) NOT NULL
);

CREATE TABLE client (
                          client_id serial PRIMARY KEY,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          phone_number VARCHAR(20)
);

CREATE TYPE currency_type AS ENUM ('USD', 'EUR', 'GBP', 'JPY', 'CAD', 'AUD', 'Other');

CREATE TYPE transaction_type AS ENUM ('Deposit', 'Withdrawal', 'Transfer', 'Payment', 'Other');

CREATE TABLE account (
                         account_id serial PRIMARY KEY,
                         account_number VARCHAR(20) UNIQUE NOT NULL,
                         balance DECIMAL(10, 2) NOT NULL,
                         client_id INT,
                         bank_id INT,
                         currency currency_type NOT NULL DEFAULT 'USD',
                         FOREIGN KEY (client_id) REFERENCES client (client_id),
                         FOREIGN KEY (bank_id) REFERENCES Bank (bank_id)
);

CREATE TABLE transaction (
                             transaction_id INT PRIMARY KEY,
                             amount DECIMAL(10, 2) NOT NULL,
                             transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             sender_account_id INT,
                             receiver_account_id INT,
                             transaction_type transaction_type NOT NULL,
                             FOREIGN KEY (sender_account_id) REFERENCES account (account_id),
                             FOREIGN KEY (receiver_account_id) REFERENCES account (account_id)
);