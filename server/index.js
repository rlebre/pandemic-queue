const express = require('express');
const bodyParser = require('body-parser');
const FakeDb = require('./fake-db');
const mongoose = require('mongoose');
const config = require('./config');
const userRoutes = require('./routes/users');
const storeRoutes = require('./routes/stores');
const ticketRoutes = require('./routes/tickets');
const subscriptionsRoutes = require('./routes/subscriptions');

const app = express();
app.use(bodyParser.json());

mongoose.connect(
    config.DB_URI,
    {
        useNewUrlParser: true,
        useCreateIndex: true,
        useUnifiedTopology: true
    }
).then(() => {
    if (process.env.NODE_ENV === 'dev') {
        const fakeDb = new FakeDb();
        fakeDb.seedDb();
    }
});

app.use('/api/v1/users', userRoutes);
app.use('/api/v1/stores', storeRoutes);
app.use('/api/v1/tickets', ticketRoutes);
app.use('/api/v1/subscriptions', subscriptionsRoutes);


const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
    console.log('Server is running.')
});

