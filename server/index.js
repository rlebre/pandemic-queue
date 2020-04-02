const express = require('express');
const bodyParser = require('body-parser');
const FakeDb = require('./fake-db');
const mongoose = require('mongoose');
const config = require('./config');
const userRoutes = require('./routes/users');

const app = express();
app.use(bodyParser.json());

mongoose.connect(config.DB_URI, { useNewUrlParser: true }).then(() => {
    if (process.env.NODE_ENV === 'dev') {
        const fakeDb = new FakeDb();
        //fakeDb.seedDb();
    }
});

//app.get('/', (req, res) => res.send({ 'resumo': 'Vais-te foder Tiago!' }))
//app.get('/hello', (req, res) => res.send({ 'resumo': 'Vais-te foder Tiago!' }))

app.use('/api/v1/users', userRoutes);


const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log('Server is running.'))