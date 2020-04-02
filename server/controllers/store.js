const Store = require('../models/store')

const { normalizeErrors } = require('../helpers/mongoose');
const moment = require('moment');

exports.createStore = function (req, res) {
    const { name, city, address, capacity } = req.body;

    if (!name || !city || !address || !capacity) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide email and password." }] });
    }

    Store.findOne({ name }, function (err, existingStore) {
        if (err) {
            return res.status(422).send({ errors: normalizeErrors(err.errors) });
        }

        if (existingStore) {
            return res.status(422).send({ errors: [{ title: "Invalid store!", detail: "Store already created." }] });
        }

        const store = new Store(
            {
                name,
                city,
                address,
                nWaiting: 0,
                lastOnQueue: null,
                lastEnteredStore: null,
                capacity,
                nWaitingTickets: []
            });

        store.save(function (err) {
            if (err) {
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            res.json({ 'registered': true });
        });
    });
}

exports.getStoreWaitingTickets = function (req, res) {
    const name = req.query.store;

    Store.where({ name })
        .populate('waitingTickets')
        .exec(function (err, foundStore) {
            if (err) {
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            return res.json(foundStore.waitingTickets ? foundStore.waitingTickets : []);
        });
}

exports.getNumberStoreWaitingTickets = function (req, res) {
    const name = req.query.store;

    Store.findOne({ name })
        .populate('waitingTickets')
        .exec(function (err, existingStore) {
            if (err) {
                console.log(err);
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (!existingStore) {
                return res.status(422).send({ errors: [{ title: "Invalid store!", detail: "Store does not exist." }] });
            }

            return res.json({ 'waiting_tickets': existingStore.waitingTickets.length });
        });
}