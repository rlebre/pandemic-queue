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

  > Body example:

  ```json
  {
    "username": "test",
    "email": "test@test.com",
    "password": "1234",
    "passwordConfirmation": "1234"
  }
  ```

- POST `localhost:3000/api/v1/users/auth`
  > Body example:
  ```json
  {
    "email": "test@test.com",
    "password": "1234"
  }
  ```

Protected:

- GET `localhost:3000/api/v1/users/get-user-details`
  > Authorization header example:
  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
  ```

#### Ticket

Protected:

- POST `localhost:3000/api/v1/tickets/create`

  > Body example:

  ```json
  {
    "store": {
      "_id": "5e863df2aa63871c66594ff0"
    }
  }
  ```

  > Authorization header example:

  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
  ```

- POST `localhost:3000/api/v1/tickets/call`

  > Body example:

  ```json
  {
    "store": {
      "_id": "5e863df2aa63871c66594ff0"
    }
  }
  ```

  > Authorization header example:

  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
  ```

- GET `localhost:3000/api/v1/tickets/exist?store={store name}`

  > Body example:

  ```json
  {
    "store": {
      "_id": "5e863df2aa63871c66594ff0"
    }
  }
  ```

  > Authorization header example:

  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
  ```

- DELETE `localhost:3000/api/v1/tickets/:storeId`


    > Authorization header example:
    ```http
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
    ```

#### Store

- POST `localhost:3000/api/v1/stores/create`

  > Body example:

  ```json
  {
    "name": "test",
    "city": "test",
    "address": "test",
    "capacity": "test"
  }
  ```

- GET `localhost:3000/api/v1/stores/number-waiting-tickets?store=example`

  > Query param: store -> name of the store

- GET `localhost:3000/api/v1/stores/waiting-tickets?store=example`

  > Query param: store -> name of the store

- GET `localhost:3000/api/v1/stores/get-store-details?store=example`

  > Query param: store -> name of the store

- GET `localhost:3000/api/v1/stores/get-store-list`

#### Subscription

- POST `localhost:3000/api/v1/subscriptions/subscribe`

  > Body example:

  ```json
  {
    "email": "test@test.com",
    "password": "1234"
  }
  ```

  > Authorization header example:

  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
  ```

- POST `localhost:3000/api/v1/subscriptions/unsubscribe`

  > Body example:

  ```json
  {
    "email": "test@test.com",
    "password": "1234"
  }
  ```

  > Authorization header example:

  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTg3N2Q5MDM0ODk2YzM4MTEwZjRmMzgiLCJ1c2VybmFtZSI6InRlc3QzIiwiaWF0IjoxNTg1OTM3ODE1LCJleHAiOjE1ODU5NDE0MTV9.Ua0R3JCuK60kTuK5fpLwbC8nbqebIZtSVsDDlUBzQeU
  ```

## Android application

- [x] Store list
- [x] Store details
- [x] Store list scroll
- [x] Store details map
- [x] Create a ticket
- [x] Cancel a ticket
- [ ] Filter store list
- [ ] Map view of the stores in Portugal
- [ ] Filter stores by location and radius

## Administration application

```
Work in progress...
```
