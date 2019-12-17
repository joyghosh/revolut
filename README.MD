# Revolut test bank server
Backend API implementation for fund transfer service

# Usage

Compile and package
> mvn clean compile package

Run tests
> mvn test

Execute the jar
> mvn exec:java -Dexec.mainClass="com.revolut.RevolutApp"

# Routes 

POST /v1/revolut/account
>{ account_num: 1, balance: 1000 }

POST /v1/revolut/account/transfer
>{amount: 10, from:1, to:2}

# Responses and status codes

> 200 Revolut account created
> 200 Funds transferred successfully.
> 403 Negative credit not allowed.
> 403 Insufficient funds.
