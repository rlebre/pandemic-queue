# Pandemic Queue App
App and server to deal with queues in markets


## Server
### Requirements
- Node.js (12.16 LTS recommended)
- MongoDB

### Run
```sh
cd server
SECRET='mysecret' NODE_ENV='dev' node index.js
```

### Services available

Short list of the web services available

#### User
Open: 

- POST `localhost:3000/api/v1/users/register`
    ```http
    Body example:
    {
        "username": "test",
        "email": "test@test.com",
        "password": "1234",
        "passwordConfirmation": "1234"
    }
    ```

- POST `localhost:3000/api/v1/users/auth`
    ```http
    Body example:
    {
        "email": "test@test.com",
        "password": "1234"
    }
    ```


Protected:

- `localhost:3000/api/v1/users/get-user-details`
    ```http
    Authorization header example:
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
    ```
#### Ticket

#### Store

#### Subscription

Protected:

- `localhost:3000/api/v1/subscriptions/subscribe`

    > **Body example:**
    ```http
    {
        "email": "test@test.com",
        "password": "1234"
    }
    ```

    > **Authorization header example:**
    ```http
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
    ```

    - `localhost:3000/api/v1/subscriptions/unsubscribe`



## Android application
```console
Work in progress...
```
