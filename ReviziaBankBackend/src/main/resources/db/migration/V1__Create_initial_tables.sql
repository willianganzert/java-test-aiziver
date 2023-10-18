CREATE TABLE contas (
                        id SERIAL PRIMARY KEY,
                        titular VARCHAR(255) NOT NULL,
                        saldo NUMERIC(19, 2) NOT NULL, -- Considernado que BigDecimal pode guardar ateh 19 digitos com 2 digitos decimais
                        numeroDaConta VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE transacoes (
                            id SERIAL PRIMARY KEY,
                            conta_origem_id INT NOT NULL,
                            conta_destino_id INT NOT NULL,
                            valor NUMERIC(19, 2) NOT NULL,
                            dataHora TIMESTAMP DEFAULT NOW(),

                            FOREIGN KEY (conta_origem_id) REFERENCES contas(id) ON DELETE CASCADE,
                            FOREIGN KEY (conta_destino_id) REFERENCES contas(id) ON DELETE CASCADE
);
