const Store = require('../models/store')

const { normalizeErrors } = require('../helpers/mongoose');

exports.createStore = function (req, res) {
    const { name, city, address, capacity, parentStore } = req.body;

    if (!name || !city || !address || !capacity) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide name, city, address and acapacity." }] });
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
                nWaitingTickets: [],
                parentStore
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

exports.getStoreDetails = function (req, res) {
    const name = req.query.name;

    Store.findOne({ name })
        .exec(function (err, existingStore) {
            if (err) {
                console.log(err);
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (!existingStore) {
                return res.status(422).send({ errors: [{ title: "Invalid store!", detail: "Store does not exist." }] });
            }

            return res.json(existingStore);
        });
}

exports.getStoreList = function (req, res) {
    Store.find({})
        .exec(function (err, stores) {
            if (err) {
                console.log(err);
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }
            return res.json(stores);
        });
}